package com.example.BiteBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiteBuddy.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

} 
