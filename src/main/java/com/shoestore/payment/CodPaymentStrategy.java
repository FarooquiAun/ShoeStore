package com.shoestore.payment;

import org.springframework.stereotype.Component;

@Component("COD")
public class CodPaymentStrategy implements PaymentStrategy{

    @Override
    public PaymentResult pay(PaymentRequest request) {
        PaymentResult result=new PaymentResult();
        result.setSuccess(true);
        result.setTransactionId("COD-"+request.getOrderId());
        result.setMessage("Cash on Delivery selected");
        return result;
    }

}
