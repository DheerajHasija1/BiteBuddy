package com.example.BiteBuddy.request;

import java.util.List;

import com.example.BiteBuddy.entities.IngredientItem;

import lombok.Data;

@Data
public class CreateFoodRequest {
    String name;
    String description;
    Long price;

    private Long categoryId;
    private List<String> images;

    // private Long restaurantId;
    private boolean vegetarian;
    private boolean seasonal;
    private List<IngredientItem> ingredientItems;
}
