package com.example.BiteBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiteBuddy.entities.IngredientItem;

public interface IngredientItemRepository extends JpaRepository<IngredientItem, Long> {

    List<IngredientItem> findByRestaurantId(Long restaurantId);
} 