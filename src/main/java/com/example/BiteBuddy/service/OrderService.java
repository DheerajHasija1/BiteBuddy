package com.example.BiteBuddy.service;

import java.util.List;

import com.example.BiteBuddy.entities.Order;
import com.example.BiteBuddy.entities.USER_ROLE;
import com.example.BiteBuddy.request.OrderRequest;

public interface OrderService {

    Order createOrder(OrderRequest order, Long userId) throws Exception;

    Order updateOrder(Long orderId, String status) throws Exception;

    void cancelOrder(Long orderId) throws Exception;

    List<Order> getUsersOrders(Long userId) throws Exception;

    List<Order> getRestaurantsOrder(Long restaurantId,String orderStatus) throws Exception;

    List<Order> getAllOrders(USER_ROLE userRole) throws Exception;

    Order findOrderById(Long orderId) throws Exception;
}