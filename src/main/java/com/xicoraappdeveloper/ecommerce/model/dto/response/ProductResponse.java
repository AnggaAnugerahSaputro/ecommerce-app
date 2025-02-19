package com.xicoraappdeveloper.ecommerce.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.xicoraappdeveloper.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class ProductResponse implements Serializable {

    private Long productId;

    private String name;

    private BigDecimal price;

    private String description;

    private BigDecimal weight;

    private Integer stockQuantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CategoryResponse> categories;

    public static ProductResponse fromProductAndCategories(Product product, List<CategoryResponse> categories) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .weight(product.getWeight())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .categories(categories)
                .build();
    }
}
