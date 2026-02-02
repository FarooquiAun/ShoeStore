package com.shoestore.order.service;

import com.shoestore.auth.entity.User;
import com.shoestore.auth.repository.UserRepository;
import com.shoestore.auth.security.CustomUserDetails;
import com.shoestore.cart.entity.Cart;
import com.shoestore.cart.entity.CartItem;
import com.shoestore.cart.repository.CartRepository;
import com.shoestore.common.enums.PaymentMethod;
import com.shoestore.common.enums.PaymentStatus;
import com.shoestore.common.util.SecurityUtil;
import com.shoestore.common.exceptions.BadRequestException;
import com.shoestore.common.exceptions.ResourceNotFoundException;
import com.shoestore.order.dto.OrderHistoryResponse;
import com.shoestore.order.dto.OrderItemResponse;
import com.shoestore.order.dto.PlaceOrderRequest;
import com.shoestore.order.entity.Order;
import com.shoestore.order.entity.OrderItem;
import com.shoestore.order.repository.OrderItemRepository;
import com.shoestore.order.repository.OrderRepository;
import com.shoestore.payment.PaymentRequest;
import com.shoestore.payment.PaymentResult;
import com.shoestore.payment.PaymentStrategyFactory;
import com.shoestore.payment.entity.Payment;
import com.shoestore.product.entity.Shoe;
import com.shoestore.product.repository.ShoeRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ShoeRepository shoeRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;

    public OrderService(CartRepository cartRepository, OrderRepository orderRepository, UserRepository userRepository, ShoeRepository shoeRepository, PaymentStrategyFactory paymentStrategyFactory) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.shoeRepository = shoeRepository;
        this.paymentStrategyFactory = paymentStrategyFactory;
    }
    @Transactional
    public Long placeOrder(PlaceOrderRequest placeOrderRequest){
        User user=getUser();
        Cart cart=cartRepository.findByUser(user).orElseThrow(
                ()-> new ResourceNotFoundException("Cart is empty")
        );
        if (cart.getItems().isEmpty()){
            throw new ResourceNotFoundException("Cart has no items");
        }
        Order order=new Order(user);
        BigDecimal total=BigDecimal.ZERO;
        for (CartItem item:cart.getItems()){
            Shoe shoe=item.getShoe();
            if(shoe.getStock()< item.getQuantity()){
                throw new BadRequestException("In sufficent stock for"+shoe.getName());
            }
            shoe.setStock(shoe.getStock()- item.getQuantity());

            OrderItem orderItem=new OrderItem(order,shoe,item.getQuantity());
            order.getOrderItemList().add(orderItem);
            total=total.add(
                    shoe.getPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    )
            );
        }
        order.setAmount(total);
        orderRepository.save(order);


        Payment payment=new Payment(order, PaymentMethod.valueOf(placeOrderRequest.getPaymentMethod()),total);

        PaymentRequest paymentRequest=new PaymentRequest();
        paymentRequest.setOrderId(order.getId());
        paymentRequest.setAmount(total);
        paymentRequest.setPaymentMethod(placeOrderRequest.getPaymentMethod());

        PaymentResult result=paymentStrategyFactory.
                getStrategy(placeOrderRequest.getPaymentMethod()).
                pay(paymentRequest);

         if (!result.isSuccess()){
             throw new ResourceNotFoundException("Payment failed");
         }


        if (result.isSuccess()){
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(result.getTransactionId());
        }else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            throw new ResourceNotFoundException("Payment Failed");
        }


        cart.getItems().clear();
        cartRepository.save(cart);

        return order.getId();

    }
    public List<OrderHistoryResponse> getMyOrders(){
        User user=getUser();
        List<Order> orders=orderRepository.findByUserOrderByCreatedAtDesc(user);
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    public OrderHistoryResponse mapToResponse(Order order){
        OrderHistoryResponse response=new OrderHistoryResponse();
        response.setOrderId(order.getId());
        response.setTotalAmount(order.getAmount());
        response.setCreatedAt(order.getCreatedAt());
        List<OrderItemResponse> resList=order.getOrderItemList().stream()
                .map(item->{
                    OrderItemResponse r=new OrderItemResponse();
                    r.setShoeId(item.getId());
                    r.setPrice(item.getPriceAtPurchase());
                    r.setShoeName(item.getShoe().getName());
                    r.setQuantity(item.getQuantity());
                    return r;
                }).collect(Collectors.toList());
        response.setItems(resList);
        return response;
    }
    private User getUser(){
        CustomUserDetails details= SecurityUtil.getCurrentUserDetails();
        return userRepository.findById(details.getUserId()).orElseThrow(
                ()-> new ResourceNotFoundException("User not found")
        );
    }
}
