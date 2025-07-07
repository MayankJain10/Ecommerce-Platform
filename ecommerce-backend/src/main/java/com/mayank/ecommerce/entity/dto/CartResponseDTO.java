package com.mayank.ecommerce.entity.dto;

public class CartResponseDTO {
    private Long cartId;
    private String message;
    private String productName;
    private int quantity;

    public CartResponseDTO(Long cartId, String message, String productName, int quantity) {
        this.cartId = cartId;
        this.message = message;
        this.productName = productName;
        this.quantity = quantity;
    }

 // Getters and Setters
    
	/**
	 * @return the cartId
	 */
	public Long getCartId() {
		return cartId;
	}

	/**
	 * @param cartId the cartId to set
	 */
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    
}

