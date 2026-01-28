package com.shoestore.payment;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentStrategyFactory {
    private final Map<String,PaymentStrategy> strategies;

    public PaymentStrategyFactory(Map<String, PaymentStrategy> strategies) {
        this.strategies = strategies;
    }
    public PaymentStrategy getStrategy(String paymentMethod){
        PaymentStrategy strategy=strategies.get(paymentMethod);
        if (strategy==null){
            throw new RuntimeException("Invalid Payment method");
        }
        return strategy;
    }
}
