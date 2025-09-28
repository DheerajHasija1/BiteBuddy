package com.example.BiteBuddy.service;

import java.util.List;

import com.example.BiteBuddy.entities.IngredientCategory;
import com.example.BiteBuddy.entities.IngredientItem;

public interface IngredientService  {

    IngredientCategory createIngredientCategory(String name,Long restaurantId) throws Exception;

    IngredientCategory findIngredientCategoryById(Long id) throws Exception;

    List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurantId) throws Exception;

    IngredientItem createIngredientItem(String ingredientName, Long restaurantId, Long categoryId) throws Exception;

    List<IngredientItem> findRestaurantIngredients(Long restaurantId) throws Exception;

    IngredientItem updateStock(Long id) throws Exception;

    List<IngredientItem> getRestaurantIngredients(Long restaurantId);
}