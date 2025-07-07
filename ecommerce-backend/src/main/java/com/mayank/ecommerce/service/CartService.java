package com.mayank.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayank.ecommerce.entity.Cart;
import com.mayank.ecommerce.entity.Product;
import com.mayank.ecommerce.entity.User;
import com.mayank.ecommerce.entity.dto.CartDTO;
import com.mayank.ecommerce.exception.CartLimitExceededException;
import com.mayank.ecommerce.exception.InsufficientStockException;
import com.mayank.ecommerce.exception.ProductNotFoundException;
import com.mayank.ecommerce.repository.CartRepository;
import com.mayank.ecommerce.repository.ProductRepository;
import com.mayank.ecommerce.repository.UserRepository;

@Service
public class CartService {
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

	/*
	 * public void addToCart(String email, CartDTO cartDTO) { User user =
	 * userRepository.findByEmail(email) .orElseThrow(() -> new
	 * RuntimeException("User not found")); Product product =
	 * productRepository.findById(cartDTO.getProductId()) .orElseThrow(() -> new
	 * RuntimeException("Product not found"));
	 * 
	 * Cart cart = cartRepository.findByUserAndProduct(user, product) .orElse(new
	 * Cart());
	 * 
	 * cart.setUser(user); cart.setProduct(product);
	 * cart.setQuantity(cart.getQuantity() + cartDTO.getQuantity());
	 * 
	 * cartRepository.save(cart);
	 * 
	 * }
	 */
    
    public Cart addToCart(String email, CartDTO cartDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(cartDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (cartDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (cartDTO.getQuantity() > product.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock available for product: " + product.getName());
        }

        Optional<Cart> optionalCart = cartRepository.findByUserAndProduct(user, product);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.setQuantity(cartDTO.getQuantity());
            return cartRepository.save(cart);
        } else {
            int cartItemCount = cartRepository.findByUser(user).size();
            if (cartItemCount >= 5) {
                throw new CartLimitExceededException("You cannot add more than 5 products to your cart.");
            }

            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setProduct(product);
            newCart.setQuantity(cartDTO.getQuantity());
            return cartRepository.save(newCart);
        }
    }





	public List<Cart> getCart(String email) {
		 User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        return cartRepository.findByUser(user);
	}
	
	 public void removeFromCart(String email, Long productId) {
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        Product product = productRepository.findById(productId)
	                .orElseThrow(() -> new RuntimeException("Product not found"));

	        Cart cart = cartRepository.findByUserAndProduct(user, product)
	                .orElseThrow(() -> new RuntimeException("Cart item not found"));
	        cartRepository.delete(cart);
	    }

	    public void clearCart(String email) {
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        List<Cart> carts = cartRepository.findByUser(user);
	        cartRepository.deleteAll(carts);
	    }


	    public Cart updateCartItem(String email, CartDTO cartDTO) {
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        Product product = productRepository.findById(cartDTO.getProductId())
	                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

	        if (cartDTO.getQuantity() <= 0) {
	            throw new IllegalArgumentException("Quantity must be greater than zero");
	        }

	        if (cartDTO.getQuantity() > product.getQuantity()) {
	            throw new InsufficientStockException("Insufficient stock available for product: " + product.getName());
	        }

	        Cart cart = cartRepository.findByUserAndProduct(user, product)
	                .orElseThrow(() -> new RuntimeException("Product not in cart"));

	        cart.setQuantity(cartDTO.getQuantity());
	        return cartRepository.save(cart);
	    }


}
