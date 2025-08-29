package com.example.BiteBuddy.request;

import java.util.List;

import com.example.BiteBuddy.entities.Address;
import com.example.BiteBuddy.entities.ContactInformation;

import lombok.Data;

@Data
public class CreateRestaurantRequest {
    private String name;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String> images;
}