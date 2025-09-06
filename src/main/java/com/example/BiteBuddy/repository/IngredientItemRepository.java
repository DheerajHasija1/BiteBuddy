package com.example.BiteBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BiteBuddy.entities.IngredientItem;

@Repository
public interface IngredientItemRepository extends JpaRepository<IngredientItem, Long> {

    List<IngredientItem> findByRestaurantId(Long restaurantId);
} 