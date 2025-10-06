package com.example.BiteBuddy.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BiteBuddy.entities.Category;
import com.example.BiteBuddy.entities.Food;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.repository.CategoryRepository;
import com.example.BiteBuddy.repository.FoodRepository;
import com.example.BiteBuddy.request.CreateFoodRequest;
import com.example.BiteBuddy.response.CategoryResponse;
import com.example.BiteBuddy.response.IngredientItemResponse;


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
       food.setVegetarian(request.isVegetarian());
       food.setSeasonal(request.isSeasonal());
       food.setIngredients(request.getIngredientItems()); 
       food.setCreatedAt(LocalDateTime.now());  
       food.setAvailable(true);

       // set images before save
       List<String> imgs = request.getImages() != null ? request.getImages() : List.of();
       food.setImages(new ArrayList<>(imgs));

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
    public List<Food> getRestaurantFoods(Long restaurantId) throws Exception{
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
        return foods;
    }

    @Override
    public List<Food> getRestaurantFoods(Long restaurantId, boolean isVeg, boolean seasonal, String foodCategory)
            throws Exception {
       List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
        
        // Filter by category first if specified
        if(foodCategory != null && !foodCategory.isEmpty()){
            foods = filterByCategory(foods, foodCategory);
        }
        
        foods = filterByVegetarian(foods, isVeg);
        
        if(seasonal){
            foods = filterBySeasonal(foods, seasonal);
        }

        return foods;
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVeg) {
        return foods.stream()
                .filter(food -> food.isVegetarian() == isVeg)
                .toList();
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean seasonal) {
        return foods.stream()
                .filter(food -> food.isSeasonal() == seasonal)
                .toList();
    }
    
    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream()
                .filter(food -> {
                    if (food.getCategory() != null) {
                        return food.getCategory().getName().equalsIgnoreCase(foodCategory);
                    }
                    return false;
                })
                .toList();
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

    // @Override
    //  public FoodResponse mapToFoodResponse(Food food) {
    //     return FoodResponse.builder()
    //             .id(food.getId())
    //             .name(food.getName())
    //             .description(food.getDescription())
    //             .price(food.getPrice())
    //             .available(food.isAvailable())
    //             .category(new CategoryResponse(food.getCategory().getId(), food.getCategory().getName()))
    //             .ingredients(food.getIngredients().stream()
    //                     .map(item -> new IngredientItemResponse(
    //                             item.getId(),
    //                             item.getName(),
    //                             item.isInStock(),
    //                             new CategoryResponse(item.getCategory().getId(), item.getCategory().getName())))
    //                     .collect(Collectors.toList()))
    //             .createdAt(food.getCreatedAt())
    //             .images(food.getImages())
    //             .seasonal(food.isSeasonal())
    //             .vegetarian(food.isVegetarian())
    //             .build();
    // }
    
}
