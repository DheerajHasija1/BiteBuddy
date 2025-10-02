package com.example.BiteBuddy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.BiteBuddy.dto.RestaurantDto;
import com.example.BiteBuddy.entities.Address;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.entities.USER_ROLE;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.repository.AddressRepository;
import com.example.BiteBuddy.repository.RestaurantRepository;
import com.example.BiteBuddy.repository.UserRepository;
import com.example.BiteBuddy.request.CreateRestaurantRequest;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {

        Address address = addressRepository.save(req.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setName(req.getName());
        restaurant.setAddress(address); 
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setImages(req.getImages());  
        restaurant.setDescription(req.getDescription());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest req) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (req.getCuisineType() != null) {
            restaurant.setCuisineType(req.getCuisineType());
        }
        if (req.getDescription() != null) {
            restaurant.setDescription(req.getDescription());
        }
        if (req.getImages() != null) {
            restaurant.setImages(req.getImages());  // Changed from setImage to setImages
        }
        if (req.getContactInformation() != null) {
            restaurant.setContactInformation(req.getContactInformation());
        }
        if (req.getName() != null) {
            restaurant.setName(req.getName());
        }
        if (req.getOpeningHours() != null) {
            restaurant.setOpeningHours(req.getOpeningHours());
        }
        if (req.getAddress() != null) {
            Address address = addressRepository.save(req.getAddress());
            restaurant.setAddress(address);
        }

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new Exception("Restaurant not found with id: " + restaurantId);
        }
        restaurantRepository.delete(restaurant);    
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurants(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Restaurant ID cannot be null");
        }
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new Exception("Restaurant not found with id: " + id));
    }

    @Override
    public Restaurant getRestaurantByUserId(Long ownerId) throws Exception {
        if (ownerId == null) {
            throw new Exception("User ID cannot be null");
        }
        
        User user = userRepository.findById(ownerId)
            .orElseThrow(() -> new Exception("User not found"));
        
       
        if (user.getRole() != USER_ROLE.ROLE_RESTAURANT_OWNER) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, 
                "Access Denied - Only restaurant owners can access this endpoint"
            );
        }

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId);
        if (restaurant == null) {
            throw new Exception("No restaurant found for this owner");
        }
        
        return restaurant;
    }
    

    @Override
    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        boolean isFavorite = false;
        for(RestaurantDto fav : user.getFavorites()){
            if(fav.getId().equals(restaurantId)){
                isFavorite = true;
                break;
            }
        }
        
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setDescription(restaurant.getDescription());
        restaurantDto.setImages(restaurant.getImages());
        restaurantDto.setTitle(restaurant.getName());
        restaurantDto.setId(restaurant.getId());

        if(isFavorite){
            user.getFavorites().removeIf(fav -> fav.getId().equals(restaurantId));
        } else {
            user.getFavorites().add(restaurantDto);
        }

        userRepository.save(user);
        return restaurantDto;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
    
}
