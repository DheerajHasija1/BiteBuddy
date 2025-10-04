package com.example.BiteBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.entities.USER_ROLE;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.request.CreateRestaurantRequest;
import com.example.BiteBuddy.service.RestaurantService;
import com.example.BiteBuddy.service.UserService;

@RestController
@RequestMapping("/admin/restaurant")
public class AdminRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody CreateRestaurantRequest req,@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);

        Restaurant restaurant = restaurantService.createRestaurant(req, user);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @PostMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody CreateRestaurantRequest req,
                                                        @RequestHeader("Authorization") String token,
                                                        @PathVariable Long restaurantId) throws Exception {     
        User user = userService.findUserByJwtToken(token);

        Restaurant restaurant = restaurantService.updateRestaurant(restaurantId,req);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long restaurantId,
                                                 @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);

        restaurantService.deleteRestaurant(restaurantId);
        return new ResponseEntity<>("Restaurant deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{restaurantId}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(@PathVariable Long restaurantId,
                                                 @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        if(user.getRole() != USER_ROLE.ROLE_RESTAURANT_OWNER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only restaurant owners can update restaurant status");
        }

        Restaurant restaurant = restaurantService.updateRestaurantStatus(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> findRestaurantByUserId(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);

        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }


}
