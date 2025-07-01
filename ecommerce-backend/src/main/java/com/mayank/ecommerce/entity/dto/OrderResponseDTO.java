package com.mayank.ecommerce.entity.dto;

import java.time.LocalDateTime;

public class OrderResponseDTO {
    private Long orderId;
    private String message;
    private double totalAmount;
    private LocalDateTime orderDate;

    public OrderResponseDTO(Long orderId, String message, double totalAmount, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.message = message;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }
    
 // Getters and setters

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

    
    
}

