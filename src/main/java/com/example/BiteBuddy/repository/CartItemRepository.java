package com.example.BiteBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiteBuddy.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
