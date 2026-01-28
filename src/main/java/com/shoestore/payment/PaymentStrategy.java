package com.shoestore.payment;

public interface PaymentStrategy {
    PaymentResult pay(PaymentRequest request);
}
