package com.xicoraappdeveloper.ecommerce.common.errors;

public class ForbiddenAccessException extends RuntimeException {

    public ForbiddenAccessException(String message) {
        super(message);
    }
}
