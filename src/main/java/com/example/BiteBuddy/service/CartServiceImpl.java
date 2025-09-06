package com.example.BiteBuddy.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BiteBuddy.entities.Cart;
import com.example.BiteBuddy.entities.CartItem;
import com.example.BiteBuddy.entities.Food;
import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.repository.CartItemRepository;
import com.example.BiteBuddy.repository.CartRepository;
import com.example.BiteBuddy.request.AddCartItemRequest;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodService foodService;

    @Override
    public CartItem addItemToCart(AddCartItemRequest request, Long userId) throws Exception {
        User user = userService.findUserById(userId); 

        Food food = foodService.findFoodById(request.getFoodId());

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        for(CartItem cartItem :cart.getItems()){
            if(cartItem.getFood().equals(food)){
                int newQuantity = cartItem.getQuantity() + request.getQuantity();
                CartItem updatedItem = updateCartItemQuantity(cartItem.getId(), newQuantity);

                // UPDATE CART TOTAL AFTER UPDATING QUANTITY
            cart = cartRepository.findByUserId(userId);
            cart.setTotalPrice(calculateCartTotal(cart));
            cartRepository.save(cart);
            
            return updatedItem;

            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setFood(food);
        newCartItem.setQuantity(request.getQuantity());
        newCartItem.setIngredients(request.getIngredients());
        newCartItem.setCart(cart);
        newCartItem.setTotalPrice(food.getPrice() * request.getQuantity());

        CartItem savedCartItem = cartItemRepository.save(newCartItem); 
        cart.getItems().add(savedCartItem);
        cartRepository.save(cart);
        return savedCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        if(cartItem.isEmpty()){
        throw new Exception("Cart item not found");
    }
    CartItem item = cartItem.get();
    item.setQuantity(quantity);

    item.setTotalPrice(item.getFood().getPrice() * quantity);
    CartItem savedItem = cartItemRepository.save(item);
    
    // ADD THIS: UPDATE CART TOTAL AFTER UPDATING ITEM
    Cart cart = item.getCart();
    cart.setTotalPrice(calculateCartTotal(cart));
    cartRepository.save(cart);
    
    return savedItem;
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, Long userId) throws Exception {
        Cart cart = cartRepository.findByUserId(userId);

        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        if(cartItem.isEmpty()){
            throw new Exception("Cart item not found");
        }

        CartItem item = cartItem.get();
        cart.getItems().remove(item);
        
        cartItemRepository.delete(item);

        cart.setTotalPrice(calculateCartTotal(cart));
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
       Long total = 0L;

       for(CartItem cartItem :cart.getItems()){
            total += cartItem.getTotalPrice();
       }
       return total;

    }

    @Override
    public Cart findCartById(Long cartId) throws Exception {
       Optional<Cart> cart = cartRepository.findById(cartId);
       if(cart.isEmpty()){
           throw new Exception("Cart not found");
       }
       return cart.get(); 
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null) {
            throw new Exception("Cart not found for user");
        }
        cart.setTotalPrice(calculateCartTotal(cart));
        return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
        Cart cart = cartRepository.findByUserId(userId);
        cart.getItems().clear();
        cart.setTotalPrice(0L);
        return cartRepository.save(cart);
    }

}
