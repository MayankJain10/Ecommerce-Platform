package com.mayank.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mayank.ecommerce.entity.Order;
import com.mayank.ecommerce.entity.Product;
import com.mayank.ecommerce.entity.User;
import com.mayank.ecommerce.entity.dto.OrderAnalyticsDTO;
import com.mayank.ecommerce.exception.ProductNotFoundException;
import com.mayank.ecommerce.repository.OrderRepository;
import com.mayank.ecommerce.repository.ProductRepository;
import com.mayank.ecommerce.repository.UserRepository;
import com.mayank.ecommerce.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	/**
	 * PUT /api/admin/deactivateUser/{email}
	 * 
	 * Deactivate User using email
	 */
	@PutMapping("/deactivateUser/{email}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String>deactivateUserByEmail(@PathVariable String email){
		adminService.deactivateUserByEmail(email);
		return ResponseEntity.ok("User deactivated successfully!");
	}
	
	// In AdminController.java
	@PutMapping("/product/stock/{id}")
	public ResponseEntity<String> updateStock(
	    @PathVariable Long id,
	    @RequestParam int quantity
	) {
	    Product product = productRepository.findById(id)
	        .orElseThrow(() -> new ProductNotFoundException("Product not found"));
	    product.setQuantity(quantity);
	    productRepository.save(product);
	    return ResponseEntity.ok("Stock updated for product: " + product.getName());
	}

	// In AdminController.java
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
	    return ResponseEntity.ok(userRepository.findAll());
	}
	
//  Dashboard Analytics (Orders)
  @Operation(summary = "Dashboard Analytics of all orders of logged-in user", description = "Dashboard Analytics of all orders of logged-in user")
  @ApiResponses(value = {
  @ApiResponse(responseCode = "200", description = "Orders fetched successfully"),
  @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  @GetMapping("/analytics")
  public OrderAnalyticsDTO getAnalytics() {
      long totalUsers = userRepository.count();
      long totalOrders = orderRepository.count();
      double totalRevenue = orderRepository.findAll()
                                  .stream()
                                  .mapToDouble(Order::getTotalAmount)
                                  .sum();

      OrderAnalyticsDTO dto = new OrderAnalyticsDTO();
      dto.setTotalUsers(totalUsers);
      dto.setTotalOrders(totalOrders);
      dto.setTotalRevenue(totalRevenue);
      return dto;
  }


}
