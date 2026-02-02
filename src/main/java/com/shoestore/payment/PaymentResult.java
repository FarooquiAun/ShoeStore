package com.shoestore.payment;

public class PaymentResult {
    private boolean success;
    private String transactionId;
    private String message;

    public String getMessage() {
        return message;
    }

    public PaymentResult() {
    }

    public PaymentResult(String message, boolean success, String transactionId) {
        this.message = message;
        this.success = success;
        this.transactionId = transactionId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
