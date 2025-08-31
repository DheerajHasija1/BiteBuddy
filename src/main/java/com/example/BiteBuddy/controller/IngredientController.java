package com.example.BiteBuddy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BiteBuddy.entities.IngredientCategory;
import com.example.BiteBuddy.entities.IngredientItem;
import com.example.BiteBuddy.request.IngredientCategoryRequest;
import com.example.BiteBuddy.request.IngredientRequest;
import com.example.BiteBuddy.service.IngredientService;

@RestController
@RequestMapping("/admin/ingredients")
public class IngredientController {
    
    @org.springframework.beans.factory.annotation.Autowired
    private IngredientService ingredientService;

    @PostMapping("category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestBody IngredientCategoryRequest request ) throws Exception {
            IngredientCategory item = ingredientService.createIngredientCategory(request.getName(), request.getRestaurantId());
            return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<IngredientItem> createIngredientItem(@RequestBody IngredientRequest request) throws Exception {
            IngredientItem item = ingredientService.createIngredientItem(request.getName(), request.getRestaurantId(), request.getCategoryId());
            return new ResponseEntity<>(item, HttpStatus.CREATED);
    } 

    @PutMapping({"/{id}/stock"})
    public ResponseEntity<IngredientItem> updateIngredientItem(@PathVariable Long id) throws Exception {
            IngredientItem item = ingredientService.updateStock(id);
            return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("restaurant/{restaurantId}")
    public ResponseEntity<List<IngredientItem>> getIngredientsByRestaurant(@PathVariable Long restaurantId) throws Exception {
            List<IngredientItem> items = ingredientService.findRestaurantIngredients(restaurantId);
            return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("restaurant/{restaurantId}/category")
    public ResponseEntity<List<IngredientCategory>> getIngredientCategoriesByRestaurant(@PathVariable Long restaurantId) throws Exception {
            List<IngredientCategory> categories = ingredientService.findIngredientCategoryByRestaurantId(restaurantId);
            return new ResponseEntity<>(categories, HttpStatus.OK);
    }

}
