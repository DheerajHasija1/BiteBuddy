package com.example.BiteBuddy.request;

import com.example.BiteBuddy.entities.Address;

import lombok.Data;

@Data
public class OrderRequest {
    private Long restaurantId;
    private Address deliveryAddress;
}
    