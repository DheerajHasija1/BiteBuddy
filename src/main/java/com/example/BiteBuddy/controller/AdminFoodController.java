package com.example.BiteBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BiteBuddy.entities.Food;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.request.CreateFoodRequest;
import com.example.BiteBuddy.response.MessageResponse;
import com.example.BiteBuddy.service.FoodService;
import com.example.BiteBuddy.service.RestaurantService;
import com.example.BiteBuddy.service.UserService;

@RestController
@RequestMapping("/admin/food")
public class AdminFoodController {
    
    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest request,
                                            @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        Restaurant restaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        Food food = foodService.createFood(request, restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long foodId,
                                                        @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);

        foodService.deleteFood(foodId);
        return new ResponseEntity<>(
            new MessageResponse("Food deleted successfully"), 
            HttpStatus.OK
        );
    }

    @PutMapping("/{foodId}/availability")
    public ResponseEntity<Food> updateFoodAvailabilityStatus(
            @PathVariable Long foodId,
            @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        Food food = foodService.updateAvailabilityStatus(foodId);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }
}
