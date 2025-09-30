package com.example.BiteBuddy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.BiteBuddy.entities.Order;
import com.example.BiteBuddy.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import com.stripe.exception.StripeException;


@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @Override
    public PaymentResponse createPaymentLink(Order order) {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("https://bitebuddy01.vercel.app/payment/success/" + order.getId())
            .setCancelUrl("https://bitebuddy01.vercel.app/payment/fail")
            .addLineItem(
                SessionCreateParams.LineItem.builder()  
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("inr")
                            .setUnitAmount((long) order.getTotalAmount() * 100) 
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("BiteBuddy Order #" + order.getId())
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build();

        try {
            Session session = Session.create(params);
            PaymentResponse response = new PaymentResponse();
            response.setPaymentUrl(session.getUrl());
            return response;
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe payment session", e);
        }
    }
}

