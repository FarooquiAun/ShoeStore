package com.shoestore.order.service;

import com.shoestore.auth.entity.User;
import com.shoestore.order.dto.AdminOrderItemResponse;
import com.shoestore.order.dto.AdminOrderResponse;
import com.shoestore.order.entity.Order;
import com.shoestore.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminOrderService {
    private final OrderRepository orderRepository;

    public AdminOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public List<AdminOrderResponse> getAllOrders(){

        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse).collect(Collectors.toList());

    }
    public AdminOrderResponse mapToResponse(Order order){
        AdminOrderResponse response=new AdminOrderResponse();
        response.setOrderId(order.getId());
        response.setOrderStatus(order.getOrderStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalAmount(order.getAmount());
        User user=order.getUser();
        response.setUserEmail(user.getEmail());
        response.setUserId(user.getId());
        List<AdminOrderItemResponse> list=order.getOrderItemList().stream()
                .map(item->{
                    AdminOrderItemResponse itemResponse=new AdminOrderItemResponse();
                    itemResponse.setShoeId(item.getId());
                    itemResponse.setShoeName(item.getShoe().getName());
                    itemResponse.setAmount(item.getPriceAtPurchase());
                    itemResponse.setQuantity(item.getQuantity());
                    return itemResponse;
                }).collect(Collectors.toList());
        response.setResponse(list);
        return response;
    }

}
