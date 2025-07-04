package com.mayank.ecommerce.service;

import com.mayank.ecommerce.config.JwtUtil;
import com.mayank.ecommerce.entity.User;
import com.mayank.ecommerce.entity.dto.UserDTO;
import com.mayank.ecommerce.exception.DisabledException;
import com.mayank.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	// Register a new user
	public void registerUser(UserDTO userDTO) {
		User user = new User();

		// Set user details from DTO
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());

		// Password encrypt using BCrypt before saving to DB
		user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

		// If role is provided and valid, set it. Otherwise default to CUSTOMER
		if (userDTO.getRole() != null
				&& (userDTO.getRole().equalsIgnoreCase("ADMIN") || userDTO.getRole().equalsIgnoreCase("CUSTOMER"))) {
			user.setRole(userDTO.getRole().toUpperCase()); // Convert to uppercase: ADMIN/CUSTOMER
		} else {
			user.setRole("CUSTOMER"); // default role
		}

		// Save user to DB
		userRepository.save(user);
	}

	// Login an existing user
	public String loginUser(UserDTO userDTO) {
		// Check if email exists
		User user = userRepository.findByEmail(userDTO.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		if(!user.isActive()) {
			throw new DisabledException("Your account has been deactivated. Contact admin.");
		}
		// Validate password
		if (!new BCryptPasswordEncoder().matches(userDTO.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		// Generate and return JWT token
		return jwtUtil.generateToken(user.getEmail());
	}

	/*
	 * public void deactivateUser(Long userId) { User user =
	 * userRepository.findById(userId).orElseThrow(()-> new
	 * UsernameNotFoundException("User not found with the given user id: " +
	 * userId)); user.setEnabled(false); userRepository.save(user);
	 * 
	 * }
	 */

	
}
