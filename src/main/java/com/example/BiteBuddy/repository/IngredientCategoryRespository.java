package com.example.BiteBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiteBuddy.entities.IngredientCategory;


public interface  IngredientCategoryRespository extends JpaRepository<IngredientCategory, Long>{

    List<IngredientCategory> findByRestaurantId(Long restaurantId);
    
}
