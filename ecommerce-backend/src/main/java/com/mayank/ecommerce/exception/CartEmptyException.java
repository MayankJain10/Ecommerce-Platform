package com.mayank.ecommerce.exception;

public class CartEmptyException extends RuntimeException{
	public CartEmptyException(String message) {
        super(message);
    }
	
}
