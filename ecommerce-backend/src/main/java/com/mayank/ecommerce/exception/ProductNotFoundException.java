package com.mayank.ecommerce.exception;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(String message) {
        super(message);
    }
}
