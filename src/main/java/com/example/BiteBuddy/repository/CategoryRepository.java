package com.example.BiteBuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiteBuddy.entities.Category;

public interface CategoryRepository  extends JpaRepository<Category, Long>{

    List<Category> findByRestaurantId(Long restaurantId);

    
} 