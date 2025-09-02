package com.example.BiteBuddy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BiteBuddy.entities.Category;
import com.example.BiteBuddy.entities.Food;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.repository.CategoryRepository;
import com.example.BiteBuddy.repository.FoodRepository;
import com.example.BiteBuddy.request.CreateFoodRequest;

@Service
public class FoodServiceImpl implements FoodService {
    
    @Autowired
    private FoodRepository foodRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Food createFood(CreateFoodRequest request, Restaurant restaurant) throws Exception {

        Category category = categoryService.findCategoryById(request.getCategoryId());
       Food food = new Food();
       food.setCategory(category);
       food.setRestaurant(restaurant);
       food.setName(request.getName());
       food.setDescription(request.getDescription());
       food.setPrice(request.getPrice());
       food.setVegetarian(request.isVeg());
       food.setSeasonal(request.isSeasonal());
       food.setIngredients(request.getIngredientItems()); 
       food.setCreatedAt(LocalDateTime.now());

       Food saveFood = foodRepository.save(food);
       restaurant.getFoods().add(saveFood);
       return saveFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {
       Food food = findFoodById(foodId);
       food.setRestaurant(null);
       foodRepository.save(food);  // we didn't want to delete the food, just disassociate it from the restaurant
    }

    @Override
    public List<Food> getRestaurantFoods(Long restaurantId, boolean isVeg, boolean seasonal, String foodCategory)
            throws Exception {
       List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
       if(isVeg){
            foods = filterByVegetarian(foods,isVeg);
       }
       if(!isVeg){
            foods = filterByNonVegetarian(foods,isVeg);
       }
       if(seasonal){
           foods = filterBySeasonal(foods,seasonal);
       }
       if(foodCategory != null && !foodCategory.isEmpty()){
           foods = filterByCategory(foods,foodCategory);
       }

       return foods;

    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVeg) {
       return foods.stream().filter(food -> food.isVegetarian() == isVeg).toList();
    }
    private List<Food> filterByNonVegetarian(List<Food> foods, boolean isVeg) {
      return foods.stream().filter(food -> food.isVegetarian() != isVeg).toList();
    }
    private List<Food> filterBySeasonal(List<Food> foods, boolean seasonal) {
       return foods.stream().filter(food -> food.isSeasonal() == seasonal).toList();
    }
    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
       return foods.stream().filter(food -> food.getCategory().equals(foodCategory)).toList();
    }

    @Override
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    // @Override
    // public List<Food> getFoodsByCategory(String categoryName) {
    //     return foodRepository.findByFoodCategoryName(categoryName);

    // }

    @Override
    public List<Food> searchFood(String keyword) throws Exception {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        return foodRepository.findById(foodId).orElseThrow(() -> new Exception("Food not found"));
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
    
}
