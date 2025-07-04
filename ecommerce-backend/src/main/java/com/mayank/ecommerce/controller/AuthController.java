package com.mayank.ecommerce.controller;

import com.mayank.ecommerce.entity.dto.UserDTO;
import com.mayank.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // All routes will start with /api/auth
public class AuthController {

	@Autowired
	private AuthService authService;

	/*
	 * POST /api/auth/register – Register a new user
	 */

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {

		// Calls service to save user
		authService.registerUser(userDTO);

		return ResponseEntity.ok("User Registered Successfully!");
	}

	/*
	 * POST /api/auth/login – Login with email & password
	 */

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {

		// Calls service to validate and return JWT token
		String token = authService.loginUser(userDTO);

		return ResponseEntity.ok(token); // Returns JWT Token
	}
	
	/**
	 * PUT /api/auth/deactivateUser/{userId}
	 * 
	 * Deactivate User
	 */
	/*
	 * @PutMapping("/deactivateUser/{userId}")
	 * 
	 * @PreAuthorize("hasRole('ADMIN')") public
	 * ResponseEntity<String>deactivateUser(@PathVariable Long userId){
	 * authService.deactivateUser(userId); return
	 * ResponseEntity.ok("User deactivated successfully!"); }
	 */
	
	
	
}
