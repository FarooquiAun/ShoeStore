package com.shoestore.payment;

import org.springframework.stereotype.Component;

@Component("UPI")
public class UpiPaymentStrategy implements PaymentStrategy{
    @Override
    public PaymentResult pay(PaymentRequest request) {
        PaymentResult result=new PaymentResult();
        result.setSuccess(true);
        result.setTransactionId("UPI-"+System.currentTimeMillis());
        result.setMessage("Upi payment successful");
        return result;
    }
}
