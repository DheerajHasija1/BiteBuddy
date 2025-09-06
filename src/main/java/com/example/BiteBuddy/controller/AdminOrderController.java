package com.example.BiteBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.BiteBuddy.entities.Order;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.service.OrderService;
import com.example.BiteBuddy.service.UserService;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {
     @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<Order>> getOrderHistory( @RequestHeader("Authorization") String jwt ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getAllOrders(user.getRole());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
