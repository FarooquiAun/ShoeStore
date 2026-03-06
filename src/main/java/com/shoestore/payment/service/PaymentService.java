package com.shoestore.payment.service;

import com.shoestore.common.enums.PaymentStatus;
import com.shoestore.common.exceptions.BadRequestException;
import com.shoestore.common.exceptions.ResourceNotFoundException;
import com.shoestore.order.entity.Order;
import com.shoestore.payment.entity.Payment;
import com.shoestore.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    @Transactional
    public void refundPayment(Order order){
        Payment payment=paymentRepository.findByOrder(order).orElseThrow(
                ()-> new ResourceNotFoundException("order not found")
        );
        if (payment.getPaymentStatus()!= PaymentStatus.SUCCESS){
            throw new BadRequestException("refund not allowed");
        }
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
    }

}
