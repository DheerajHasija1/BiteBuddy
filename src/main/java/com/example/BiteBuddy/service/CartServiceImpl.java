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
    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Food food = foodService.findFoodById(request.getFoodId());

        Cart cart = cartRepository.findByUserId(user.getId());
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
            cart = cartRepository.findByUserId(user.getId());
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
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartRepository.findByUserId(user.getId());

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
    public Cart findCartByUserId(String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return cartRepository.findByUserId(user.getId());
    }

    @Override
    public Cart clearCart(String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartRepository.findByUserId(user.getId());
        cart.getItems().clear();
        cart.setTotalPrice(0L);
        return cartRepository.save(cart);
    }

}
