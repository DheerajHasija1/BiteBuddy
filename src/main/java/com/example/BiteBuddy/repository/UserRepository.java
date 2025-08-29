package com.example.BiteBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiteBuddy.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);
     
    
}