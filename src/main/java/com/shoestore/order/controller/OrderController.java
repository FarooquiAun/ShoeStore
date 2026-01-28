package com.shoestore.order.controller;

import com.shoestore.order.dto.OrderHistoryResponse;
import com.shoestore.order.dto.PlaceOrderRequest;
import com.shoestore.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<Long> placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {
        return ResponseEntity.ok(orderService.placeOrder(placeOrderRequest));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderHistoryResponse>> getOrders(){
        return ResponseEntity.ok(orderService.getMyOrders());
    }
}
