package com.mayank.ecommerce.entity.dto;

public class OrderAnalyticsDTO {
    private long totalOrders;
    private double totalRevenue;
    private long totalUsers;
    
    // Getters & Setters
    
	/**
	 * @return the totalOrders
	 */
	public long getTotalOrders() {
		return totalOrders;
	}
	/**
	 * @param totalOrders the totalOrders to set
	 */
	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}
	/**
	 * @return the totalRevenue
	 */
	public double getTotalRevenue() {
		return totalRevenue;
	}
	/**
	 * @param totalRevenue the totalRevenue to set
	 */
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	/**
	 * @return the totalUsers
	 */
	public long getTotalUsers() {
		return totalUsers;
	}
	/**
	 * @param l the totalUsers to set
	 */
	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

  
    
    
}

