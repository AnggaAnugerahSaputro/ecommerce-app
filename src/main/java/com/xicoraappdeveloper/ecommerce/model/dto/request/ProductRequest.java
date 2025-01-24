package com.xicoraappdeveloper.ecommerce.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "the product name cannot be empty")
    @Size(min = 3, max = 100, message = "Product names must be between 2 and 100 characters")
    String name;

    @NotNull(message = "description cannot be empty")
    @Size(max = 1000, message = "Product descriptions should not exceed 1000 characters")
    String description;

    @NotNull(message = "price cannot be empty")
    @Positive(message = "price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "The price must have a maximum of 10 digits and 2 digits after the comma")
    BigDecimal price;


}
