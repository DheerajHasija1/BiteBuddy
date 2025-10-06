package com.example.BiteBuddy.service;

import java.util.List;

import com.example.BiteBuddy.entities.Food;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.request.CreateFoodRequest;

public interface FoodService {

    public Food createFood(CreateFoodRequest request, Restaurant restaurant) throws Exception;

    void deleteFood(Long foodId) throws Exception;
    
    public List<Food> getRestaurantFoods(Long restaurantId) throws Exception;

    List<Food> getRestaurantFoods(Long restaurantId,boolean isVeg, boolean seasonal,String foodCategory) throws Exception;

    List<Food> getAllFoods();

    // List<Food> getFoodsByCategory(String category) throws Exception;

    public List<Food> searchFood(String keyword) throws Exception;

    public Food findFoodById(Long foodId) throws Exception;

    public Food updateAvailabilityStatus(Long foodId) throws Exception;

    // public FoodResponse mapToFoodResponse(Food food);
}