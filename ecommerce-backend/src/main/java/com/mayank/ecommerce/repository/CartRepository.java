package com.mayank.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayank.ecommerce.entity.Cart;
import com.mayank.ecommerce.entity.Product;
import com.mayank.ecommerce.entity.User;

public interface CartRepository  extends JpaRepository<Cart, Long>{
	
	List<Cart> findByUser(User user);
	Optional<Cart>findByUserAndProduct(User user, Product product);
	Optional<Cart> findByUserAndProductId(User user, Long productId);

}
