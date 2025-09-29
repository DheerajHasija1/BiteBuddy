package com.example.BiteBuddy.service;

import com.example.BiteBuddy.entities.Order;
import com.example.BiteBuddy.response.PaymentResponse;

public interface PaymentService {

    public PaymentResponse createPaymentLink(Order order);
    
} 
