package com.example.BiteBuddy.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BiteBuddy.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
