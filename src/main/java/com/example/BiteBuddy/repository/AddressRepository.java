package com.example.BiteBuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiteBuddy.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
