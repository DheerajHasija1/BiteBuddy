package com.example.BiteBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BiteBuddy.entities.Order;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.request.OrderRequest;
import com.example.BiteBuddy.service.OrderService;
import com.example.BiteBuddy.service.UserService;

@RestController
@RequestMapping("/orders")
public class SellerOrderController {
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long id,
                                                        @RequestParam(required = false) String orderStatus,
                                                        @RequestHeader("Authorization") String jwt ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getRestaurantsOrder(id, orderStatus);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId,
                                                   @PathVariable String orderStatus,
                                                   @RequestHeader("Authorization") String jwt ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.updateOrder(orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    

}   

