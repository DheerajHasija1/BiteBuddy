package com.example.BiteBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BiteBuddy.entities.Address;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.service.AddressService;
import com.example.BiteBuddy.service.UserService;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Address> createAddress(@RequestBody Address address, @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Address createAddress = addressService.createAddress(address,user.getId());
        return new ResponseEntity<>(createAddress,HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Address>> getUserAddresses(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        List<Address> addresses = addressService.getUserAddresses(user.getId());
        return new ResponseEntity<>(addresses,HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId,@RequestHeader("Authorization") String jwt) throws Exception{
        addressService.deleteAddress(addressId);
        return new ResponseEntity<>("Address Delete Successfully",HttpStatus.OK);
    } 
}