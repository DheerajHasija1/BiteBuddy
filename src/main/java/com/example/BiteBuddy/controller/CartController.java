package com.example.BiteBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BiteBuddy.entities.Cart;
import com.example.BiteBuddy.entities.CartItem;
import com.example.BiteBuddy.request.AddCartItemRequest;
import com.example.BiteBuddy.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddCartItemRequest request ,
                                                  @RequestHeader("Authorization") String jwt ) throws Exception {
        CartItem item = cartService.addItemToCart(request, jwt);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}/quantity")
    public ResponseEntity<CartItem> updateCartItemQuantity(@PathVariable Long cartItemId,
                                                           @RequestParam int quantity,
                                                           @RequestHeader("Authorization") String jwt) throws Exception {
        CartItem item = cartService.updateCartItemQuantity(cartItemId, quantity);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @DeleteMapping("/{cartItemId}/remove")
    public ResponseEntity<Cart> removeCartItemEntity(@PathVariable Long cartItemId,
                                                           @RequestHeader("Authorization") String jwt) throws Exception {
        Cart item = cartService.removeItemFromCart(cartItemId, jwt);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PutMapping("/clear")
    public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization") String jwt) throws Exception {
        Cart cart = cartService.clearCart(jwt);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws Exception {
        Cart cart = cartService.findCartByUserId(jwt);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }


}
