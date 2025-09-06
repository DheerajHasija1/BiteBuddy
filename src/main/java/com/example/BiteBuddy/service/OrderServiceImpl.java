package com.example.BiteBuddy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BiteBuddy.entities.Address;
import com.example.BiteBuddy.entities.Cart;
import com.example.BiteBuddy.entities.CartItem;
import com.example.BiteBuddy.entities.Order;
import com.example.BiteBuddy.entities.OrderItem;
import com.example.BiteBuddy.entities.Restaurant;
import com.example.BiteBuddy.entities.USER_ROLE;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.repository.AddressRepository;
import com.example.BiteBuddy.repository.OrderItemRepository;
import com.example.BiteBuddy.repository.OrderRepository;
import com.example.BiteBuddy.repository.UserRepository;
import com.example.BiteBuddy.request.OrderRequest;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;    


    @Override
    public Order createOrder(OrderRequest order, Long UserId) throws Exception {
        User user = userService.findUserById(UserId);

        Address deliveryAddress = order.getDeliveryAddress();
        Address savedAddress = addressRepository.save(deliveryAddress);

        if(!user.getAddresses().contains(deliveryAddress)) {
            user.getAddresses().add(savedAddress);
            userRepository.save(user);
        }

        Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());

        Order newOrder = new Order();
        newOrder.setCustomer(user);
        newOrder.setRestaurant(restaurant);
        newOrder.setOrderStatus("PENDING");
        newOrder.setCreatedAt(new Date());
        newOrder.setDeliveryAddress(savedAddress);

        Order savedOrder = orderRepository.save(newOrder);

        Cart cart = cartService.findCartByUserId(UserId);
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setOrder(savedOrder);

            orderItems.add(orderItemRepository.save(orderItem));    
        }
        Long totalPrice = cartService.calculateCartTotal(cart);
        newOrder.setOrderItems(orderItems);
        newOrder.setTotalAmount(totalPrice);

        savedOrder = orderRepository.save(savedOrder);
        cartService.clearCart(UserId);
        restaurant.getOrders().add(savedOrder);

        return savedOrder;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        if(orderStatus.equals("OUT_FOR_DELIVERY") 
                || orderStatus.equals("DELIVERED") 
                || orderStatus.equals("COMPLETED")
                || orderStatus.equals("PENDING")
            ) { 
                order.setOrderStatus(orderStatus);
                return orderRepository.save(order);
        } else {
            throw new Exception("Please provide a valid status");
        }
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
        Order order = findOrderById(orderId);
        orderRepository.deleteById(order.getId());
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) {
            throw new Exception("Order not found");
        }
        return order;
    }
    

    @Override
    public List<Order> getUsersOrders(Long userId) throws Exception {
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {
       List<Order> orders=orderRepository.findByRestaurantId(restaurantId);
       if(orderStatus != null) {
           orders = orders.stream()
                   .filter(order -> order.getOrderStatus().equals(orderStatus))
                   .collect(Collectors.toList());
       }
       return orders;
    }

    @Override
    public List<Order> getAllOrders(USER_ROLE userRole) throws Exception {
        List<Order> orders = orderRepository.findAll();
        if(userRole == USER_ROLE.ROLE_ADMIN) {
           return orders;
        }else{
            throw new Exception("Access Denied");
        }
    }
}