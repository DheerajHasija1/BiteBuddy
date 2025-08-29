package com.example.BiteBuddy.service;

import java.util.List;

import com.example.BiteBuddy.dto.RestaurantDto;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.request.CreateRestaurantRequest;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest req,User user);

    public Restaurant updateRestaurant(Long restaurantId,CreateRestaurantRequest req) throws Exception;

    public void deleteRestaurant(Long restaurantId) throws Exception;

    public List<Restaurant> getAllRestaurants();

    public List<Restaurant> searchRestaurants(String keyword);

    public Restaurant findRestaurantById(Long id) throws Exception;

    public Restaurant getRestaurantByUserId(Long userId) throws Exception;

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;

    public Restaurant updateRestaurantStatus(Long id) throws Exception;

    

}