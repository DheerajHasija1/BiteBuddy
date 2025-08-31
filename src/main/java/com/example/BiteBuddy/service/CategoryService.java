package com.example.BiteBuddy.service;

import java.util.List;

import com.example.BiteBuddy.entities.Category;


public interface CategoryService {
    
    Category createCategory(String name,Long userId) throws Exception;

    List<Category> findCategoryByRestaurantId(Long restaurantId) throws Exception;

    Category findCategoryById(Long categoryId) throws Exception;

    
}
