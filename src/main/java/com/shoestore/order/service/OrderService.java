package com.shoestore.order.service;

import com.shoestore.auth.entity.User;
import com.shoestore.auth.repository.UserRepository;
import com.shoestore.auth.security.CustomUserDetails;
import com.shoestore.cart.entity.Cart;
import com.shoestore.cart.entity.CartItem;
import com.shoestore.cart.repository.CartRepository;
import com.shoestore.common.util.SecurityUtil;
import com.shoestore.order.entity.Order;
import com.shoestore.order.entity.OrderItem;
import com.shoestore.order.repository.OrderItemRepository;
import com.shoestore.order.repository.OrderRepository;
import com.shoestore.product.entity.Shoe;
import com.shoestore.product.repository.ShoeRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ShoeRepository shoeRepository;

    public OrderService(CartRepository cartRepository, OrderRepository orderRepository, UserRepository userRepository, ShoeRepository shoeRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.shoeRepository = shoeRepository;
    }
    @Transactional
    public Long placeOrder(){
        User user=getUser();
        Cart cart=cartRepository.findByUser(user).orElseThrow(
                ()-> new RuntimeException("Cart is empty")
        );
        if (cart.getItems().isEmpty()){
            throw new RuntimeException("Cart has no items");
        }
        Order order=new Order(user);
        BigDecimal total=BigDecimal.ZERO;
        for (CartItem item:cart.getItems()){
            Shoe shoe=item.getShoe();
            if(shoe.getStock()< item.getQuantity()){
                throw new RuntimeException("In sufficent stock for"+shoe.getName());
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

        cart.getItems().clear();
        cartRepository.save(cart);

        return order.getId();

    }
    private User getUser(){
        CustomUserDetails details= SecurityUtil.getCurrentUserDetails();
        return userRepository.findById(details.getUserId()).orElseThrow(
                ()-> new RuntimeException("User not found")
        );
    }
}
