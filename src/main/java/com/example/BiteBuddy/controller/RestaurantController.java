package com.example.BiteBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BiteBuddy.dto.RestaurantDto;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.service.RestaurantService;
import com.example.BiteBuddy.service.UserService;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        List<Restaurant> restaurant = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Restaurant> findRestaurantById(@PathVariable Long id, @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);

        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @PutMapping("{restaurantId}/add-favorites")
    public ResponseEntity<RestaurantDto> addToFavorites(@RequestHeader("Authorization") String token,
                                                      @PathVariable Long restaurantId) throws Exception {
        User user = userService.findUserByJwtToken(token);
                                                        
        RestaurantDto restaurant = restaurantService.addToFavorites(restaurantId, user);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }


}