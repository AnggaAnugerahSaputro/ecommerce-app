package com.xicoraappdeveloper.ecommerce.common.errors;

public class ResourcesNotFoundException extends RuntimeException {
    public ResourcesNotFoundException(String message) {
        super(message);
    }
}
