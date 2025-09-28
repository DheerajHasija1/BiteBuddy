package com.example.BiteBuddy.service;

import java.util.List;

import com.example.BiteBuddy.entities.Address;

public interface AddressService {
    
    Address createAddress(Address address, Long userId) throws Exception;
    
    List<Address> getUserAddresses(Long userId) throws Exception;
    
    void deleteAddress(Long addressId) throws Exception;
}