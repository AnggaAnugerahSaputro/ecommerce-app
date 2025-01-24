package com.xicoraappdeveloper.ecommerce.controller;

import com.xicoraappdeveloper.ecommerce.common.errors.BadRequestException;
import com.xicoraappdeveloper.ecommerce.common.errors.ResourcesNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/bad-request")
    public ResponseEntity<String> badRequestError() {
        throw new BadRequestException("ada error bad request");
    }

    @GetMapping("/generic-error")
    public ResponseEntity<String> genericError() {
        throw new RuntimeException("generic error");
    }

    @GetMapping("/not-found")
    public ResponseEntity<String> notFoundError() {
        throw new ResourcesNotFoundException("data tidak ditemukan");
    }
}
