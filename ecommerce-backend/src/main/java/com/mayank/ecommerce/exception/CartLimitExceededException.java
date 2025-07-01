package com.mayank.ecommerce.exception;

public class CartLimitExceededException extends RuntimeException {
	public CartLimitExceededException(String message) {
		super(message);
	}

}
