package com.mayank.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mayank.ecommerce.entity.Order;
import com.mayank.ecommerce.entity.User;
import com.mayank.ecommerce.entity.dto.OrderAnalyticsDTO;
import com.mayank.ecommerce.repository.OrderRepository;
import com.mayank.ecommerce.repository.UserRepository;

@Service
public class AdminService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;


	/**
	 * Method to deactivate user by email.
	 * @param email
	 * 
	 */
	public void deactivateUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with the given mail id: " + email));
		user.setActive(false);
		userRepository.save(user);
		
	}

}
