package com.example.BiteBuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BiteBuddy.entities.Address;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.repository.AddressRepository;
import com.example.BiteBuddy.repository.UserRepository;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Override
    public Address createAddress(Address address, Long userId) throws Exception {
       User user = userService.findUserById(userId);
       address.setUser(user);

      Address savedAddress = addressRepository.save(address);

      if(!user.getAddresses().contains(savedAddress)){
        user.getAddresses().add(savedAddress);
        userRepository.save(user);
      }

      return savedAddress;
    }

    @Override
    public List<Address> getUserAddresses(Long userId) throws Exception {
        User user = userService.findUserById(userId);
        return user.getAddresses();
    }

    @Override
    public void deleteAddress(Long addressId) throws Exception {
        Address address = addressRepository.findById(addressId).orElseThrow(
            () -> new Exception("address not found with id: " + addressId));
        
        addressRepository.delete(address);
    }
    
}
