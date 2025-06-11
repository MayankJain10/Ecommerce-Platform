package com.mayank.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayank.ecommerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product>findByNameContainingIgnoreCase(String keyword);

}
