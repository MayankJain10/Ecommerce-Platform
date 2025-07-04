package com.mayank.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mayank.ecommerce.entity.Cart;
import com.mayank.ecommerce.entity.Order;
import com.mayank.ecommerce.entity.OrderItem;
import com.mayank.ecommerce.entity.Product;
import com.mayank.ecommerce.entity.User;
import com.mayank.ecommerce.entity.dto.OrderAnalyticsDTO;
import com.mayank.ecommerce.entity.dto.OrderItemDTO;
import com.mayank.ecommerce.entity.dto.OrderRequestDTO;
import com.mayank.ecommerce.enums.OrderStatus;
import com.mayank.ecommerce.exception.CartEmptyException;
import com.mayank.ecommerce.exception.ResourceNotFoundException;
import com.mayank.ecommerce.repository.CartRepository;
import com.mayank.ecommerce.repository.OrderRepository;
import com.mayank.ecommerce.repository.ProductRepository;
import com.mayank.ecommerce.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;
    

    public Order placeOrder(OrderRequestDTO request, String userEmail) {
    	logger.info("User Email:- ", userEmail);
    	
        User user = userRepository.findByEmail(userEmail)
        		.orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (OrderItemDTO dto : request.getItems()) {
            Product product = productRepository.findById(dto.getProductId())
            		.orElseThrow(() -> new RuntimeException("Product not found with the given id: " + dto.getProductId()));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPrice(product.getPrice() * dto.getQuantity());
            item.setOrder(order);

            total += item.getPrice();
            orderItems.add(item);
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);

        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(String email) {
    	 User user = userRepository.findByEmail(email)
    		        .orElseThrow(() -> new RuntimeException("User not found"));
    	 return orderRepository.findByUserAndCancelledFalse(user);

    }

	/*
	 * public Order placeOrderFromCart(String email) { User user =
	 * userRepository.findByEmail(email).orElseThrow(() -> new
	 * RuntimeException("User not found"));
	 * 
	 * List<Cart>cartItems = cartRepository.findByUser(user);
	 * if(cartItems.isEmpty()) { throw new
	 * CartEmptyException("Your cart is empty. Please add items before placing an order."
	 * ); }
	 * 
	 * Order order = new Order(); order.setUser(user);
	 * order.setOrderDate(LocalDateTime.now());
	 * 
	 * List<OrderItem>orderItems = new ArrayList<>(); double total = 0.0;
	 * 
	 * for(Cart cart: cartItems) { OrderItem item = new OrderItem();
	 * item.setOrder(order); item.setProduct(cart.getProduct());
	 * item.setQuantity(cart.getQuantity());
	 * item.setPrice(cart.getProduct().getPrice()); orderItems.add(item);
	 * 
	 * total += cart.getProduct().getPrice() * cart.getQuantity();
	 * 
	 * }
	 * 
	 * order.setItems(orderItems); order.setTotalAmount(total);
	 * 
	 * Order savedOrder = orderRepository.save(order);
	 * 
	 * cartRepository.deleteAll(cartItems); // Clear cart after placing order
	 * 
	 * return savedOrder;
	 * 
	 * }
	 */
    
    @Transactional
    public Order placeOrderFromCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new CartEmptyException("Your cart is empty. Please add items before placing an order.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (Cart cart : cartItems) {
            Product product = cart.getProduct();

            // Validate stock before placing order
            if (product.getQuantity() < cart.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // 🛒 Reduce product stock
            product.setQuantity(product.getQuantity() - cart.getQuantity());
            productRepository.save(product); // Save updated stock

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(cart.getQuantity());
            item.setPrice(product.getPrice());

            orderItems.add(item);
            total += product.getPrice() * cart.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        cartRepository.deleteAll(cartItems); // Clear cart after order placed

        return savedOrder;
    }
    

    public void cancelOrder(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Not authorized");
        }

        order.setCancelled(true);
        orderRepository.save(order);
    }





}
