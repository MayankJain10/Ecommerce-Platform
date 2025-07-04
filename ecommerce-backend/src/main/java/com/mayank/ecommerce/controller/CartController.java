package com.mayank.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mayank.ecommerce.config.JwtUtil;
import com.mayank.ecommerce.entity.Cart;
import com.mayank.ecommerce.entity.dto.CartDTO;
import com.mayank.ecommerce.entity.dto.CartResponseDTO;
import com.mayank.ecommerce.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart APIs", description = "Manage user's cart operations")
public class CartController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CartService cartService;

    @Operation(summary = "Add product to cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or cart full")
    })
    @PostMapping("/addTo/cart")
    public ResponseEntity<CartResponseDTO> addToCart(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody CartDTO cartDTO) {
        
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        Cart cart = cartService.addToCart(email, cartDTO);
        CartResponseDTO response = new CartResponseDTO(
            cart.getId(), "Product added to cart", cart.getProduct().getName(), cart.getQuantity()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all cart items")
    @GetMapping("/get/cart")
    public List<Cart> getCart(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        return cartService.getCart(email);
    }

    @Operation(summary = "Remove product from cart by productId")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long productId) {
        
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        cartService.removeFromCart(email, productId);
        return ResponseEntity.ok("Item Removed from cart");
    }

    @Operation(summary = "Clear all items from cart")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        cartService.clearCart(email);
        return ResponseEntity.ok("Cart cleared");
    }

    @Operation(summary = "Update quantity of a cart item")
    @PutMapping("/update/cart")
    public ResponseEntity<CartResponseDTO> updateCartItem(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody CartDTO cartDTO) {
        
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        Cart cart = cartService.updateCartItem(email, cartDTO);
        CartResponseDTO response = new CartResponseDTO(
            cart.getId(), "Cart item updated successfully", cart.getProduct().getName(), cart.getQuantity()
        );
        return ResponseEntity.ok(response);
    }
}
