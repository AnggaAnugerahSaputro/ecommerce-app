package com.xicoraappdeveloper.ecommerce.common.errors;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
// user not found