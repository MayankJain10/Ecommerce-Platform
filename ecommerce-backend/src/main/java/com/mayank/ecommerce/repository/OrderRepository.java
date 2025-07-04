package com.mayank.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mayank.ecommerce.entity.Order;
import com.mayank.ecommerce.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order>findByUser(User user);
	Order findByIdAndUserEmail(Long id, String email);
	
	// OrderRepository.java
	@Query("SELECT o FROM Order o WHERE o.user.email = :email AND o.cancelled = false ORDER BY o.orderDate DESC")
	List<Order> findActiveOrdersByEmail(@Param("email") String email);

	List<Order> findByUserAndCancelledFalse(User user);


}
