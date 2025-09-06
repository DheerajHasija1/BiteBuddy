package com.example.BiteBuddy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BiteBuddy.entities.Category;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.service.CategoryService;
import com.example.BiteBuddy.service.RestaurantService;
import com.example.BiteBuddy.service.UserService;

@RestController
@RequestMapping("/categories") 
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired 
    private RestaurantService restaurantService;

    @PostMapping("/admin")
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category,
            @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);

        Category createCategory = categoryService.createCategory(category.getName(), user.getId());
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<Category>> getRestaurantCategory(
            @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByJwtToken(token);
        Restaurant restaurant=restaurantService.getRestaurantByUserId(user.getId());
        List<Category> categories = categoryService.findCategoryByRestaurantId(restaurant.getId());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
