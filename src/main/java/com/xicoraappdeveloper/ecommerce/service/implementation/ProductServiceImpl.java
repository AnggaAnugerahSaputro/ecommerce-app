package com.xicoraappdeveloper.ecommerce.service.implementation;

import com.xicoraappdeveloper.ecommerce.common.errors.ResourcesNotFoundException;
import com.xicoraappdeveloper.ecommerce.entity.Category;
import com.xicoraappdeveloper.ecommerce.entity.Product;
import com.xicoraappdeveloper.ecommerce.entity.ProductCategory.ProductCategoryId;
import com.xicoraappdeveloper.ecommerce.entity.ProductCategory;
import com.xicoraappdeveloper.ecommerce.model.dto.request.ProductRequest;
import com.xicoraappdeveloper.ecommerce.model.dto.response.CategoryResponse;
import com.xicoraappdeveloper.ecommerce.model.dto.response.PaginatedProductResponse;
import com.xicoraappdeveloper.ecommerce.model.dto.response.ProductResponse;
import com.xicoraappdeveloper.ecommerce.repository.CategoryRepository;
import com.xicoraappdeveloper.ecommerce.repository.ProductCategoryRepository;
import com.xicoraappdeveloper.ecommerce.repository.ProductRepository;
import com.xicoraappdeveloper.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream().map(product -> {
                    List<CategoryResponse> productCategories = getProductCategories(product.getProductId());
                    return ProductResponse.fromProductAndCategories(product, productCategories);
                })
                .toList();
    }

    @Override
    public Page<ProductResponse> findByPage(Pageable pageable) {
        return productRepository.findByPageable(pageable)
                .map(product -> {
                    List<CategoryResponse> productCategories = getProductCategories(product.getProductId());
                    return ProductResponse.fromProductAndCategories(product, productCategories);
                });
    }

    @Override
    public Page<ProductResponse> findByNameAndPageable(String name, Pageable pageable) {
        name = "%" + name + "%";
        name = name.toLowerCase();
        return productRepository.findByNamePageable(name, pageable)
                .map(product -> {
                    List<CategoryResponse> productCategories = getProductCategories(product.getProductId());
                    return ProductResponse.fromProductAndCategories(product, productCategories);
                });
    }

    @Override
    public ProductResponse findById(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourcesNotFoundException("Product not found with id: " + productId));
        List<CategoryResponse> productCategories = getProductCategories(productId);
        return ProductResponse.fromProductAndCategories(existingProduct, productCategories);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        List<Category> categories = getCategoriesByIds(productRequest.getCategoryIds());

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .userId(productRequest.getUser().getUserId())
                .weight(productRequest.getWeight())
                .build();

        Product createdProduct = productRepository.save(product);

        List<ProductCategory> productCategories = categories.stream()
                .map(category -> {
                    ProductCategory productCategory = new ProductCategory();
                    ProductCategoryId productCategoryId = new ProductCategoryId();
                    productCategoryId.setCategoryId(category.getCategoryId());
                    productCategoryId.setProductId(createdProduct.getProductId());
                    productCategory.setId(productCategoryId);
                    return productCategory;
                })
                .toList();
        productCategoryRepository.saveAll(productCategories);

        List<CategoryResponse> categoryResponseList = categories.stream()
                .map(CategoryResponse::fromCategory)
                .toList();

        return ProductResponse.fromProductAndCategories(createdProduct, categoryResponseList);
    }

    @Override
    @Transactional
    public ProductResponse update(Long productId, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourcesNotFoundException("Product not found with id: " + productId));

        List<Category> categories = getCategoriesByIds(productRequest.getCategoryIds());
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStockQuantity(productRequest.getStockQuantity());
        existingProduct.setWeight(productRequest.getWeight());
        productRepository.save(existingProduct);

        List<ProductCategory> existingProductCategories = productCategoryRepository.findCategoriesByProductId(productId);
        productCategoryRepository.deleteAll(existingProductCategories);

        List<ProductCategory> productCategories = categories.stream()
                .map(category -> {
                    ProductCategory productCategory = new ProductCategory();
                    ProductCategoryId productCategoryId = new ProductCategoryId();
                    productCategoryId.setCategoryId(category.getCategoryId());
                    productCategoryId.setProductId(productId);
                    productCategory.setId(productCategoryId);
                    return productCategory;
                })
                .toList();
        productCategoryRepository.saveAll(productCategories);

        List<CategoryResponse> categoryResponseList = categories.stream()
                .map(CategoryResponse::fromCategory)
                .toList();

        return ProductResponse.fromProductAndCategories(existingProduct, categoryResponseList);
    }

    @Override
    @Transactional
    public void delete(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourcesNotFoundException("Product not found with id: " + productId));
        List<ProductCategory> productCategories = productCategoryRepository.findCategoriesByProductId(
                productId);

        productCategoryRepository.deleteAll(productCategories);
        productRepository.delete(existingProduct);
    }

    @Override
    public PaginatedProductResponse convertProductPage(Page<ProductResponse> productResponsePage) {
        return PaginatedProductResponse.builder()
                .data(productResponsePage.getContent())
                .pageNo(productResponsePage.getNumber())
                .pageSize(productResponsePage.getSize())
                .totalElements(productResponsePage.getTotalElements())
                .totalPages(productResponsePage.getTotalPages())
                .last(productResponsePage.isLast())
                .build();
    }

    private List<Category> getCategoriesByIds(List<Long> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() ->{
                            return new ResourcesNotFoundException("category not found for id : " + categoryId);
                        }))
                .toList();
    }

    private List<CategoryResponse> getProductCategories(Long productId) {
        List<ProductCategory> productCategories = productCategoryRepository.findCategoriesByProductId(
                productId);
        List<Long> categoryIds = productCategories.stream()
                .map(productCategory -> productCategory.getId().getCategoryId())
                .toList();
        return categoryRepository.findAllById(categoryIds)
                .stream().map(CategoryResponse::fromCategory)
                .toList();
    }
}
