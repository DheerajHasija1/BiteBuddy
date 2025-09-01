package com.example.BiteBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BiteBuddy.entities.Food;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.service.FoodService;
import com.example.BiteBuddy.service.RestaurantService;
import com.example.BiteBuddy.service.UserService;

@RestController
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private FoodService foodService;
 
    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name) throws Exception {
        List<Food> food = foodService.searchFood(name);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Food>> getAllFood(){
        List<Food> food = foodService.getAllFoods();
        return new ResponseEntity<>(food,HttpStatus.OK);
    }


    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFood(@RequestParam(defaultValue = "false") boolean Veg,
                                            @PathVariable Long restaurantId,
                                            @RequestParam(defaultValue = "false") boolean seasonal,
                                            @RequestParam(required = false) String foodCategory,
                                            @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);

        List<Food> food = foodService.getRestaurantFoods(restaurantId, Veg, seasonal, foodCategory);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

}
