package com.example.BiteBuddy.service;


import com.example.BiteBuddy.entities.Cart;
import com.example.BiteBuddy.entities.CartItem;
import com.example.BiteBuddy.request.AddCartItemRequest;

public interface CartService {

    CartItem addItemToCart(AddCartItemRequest request ,String jwt) throws Exception;

    CartItem updateCartItemQuantity(Long cartItemId,int quantity) throws Exception;

    Cart removeItemFromCart(Long cartItemId,String jwt) throws Exception;

    Long calculateCartTotal(Cart cart) throws Exception;

    Cart findCartById(Long cartId) throws Exception;

    Cart findCartByUserId(String jwt) throws Exception;

    Cart clearCart(String jwt) throws Exception;
} 