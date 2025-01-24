package com.xicoraappdeveloper.ecommerce.controller;

import com.xicoraappdeveloper.ecommerce.model.dto.request.ProductRequest;
import com.xicoraappdeveloper.ecommerce.model.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable(value = "id") Long productId) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name("product : " + productId)
                        .price(BigDecimal.ONE)
                        .description("description")
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProducts() {
        return ResponseEntity.ok(
                List.of(
                        ProductResponse.builder()
                                .name("product 1")
                                .price(BigDecimal.ONE)
                                .description("description")
                                .build(),
                        ProductResponse.builder()
                                .name("product 2")
                                .price(BigDecimal.ONE)
                                .description("description")
                                .build()
                )

        );
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(productRequest.getName())
                        .price(productRequest.getPrice())
                        .description(productRequest.getDescription())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updatedProduct(
            @PathVariable(value = "id") Long productId,
            @RequestBody @Valid ProductRequest productRequest
    ) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(productRequest.getName()+ " " + productId)
                        .price(productRequest.getPrice())
                        .description(productRequest.getDescription())
                        .build()
        );
    }
}
