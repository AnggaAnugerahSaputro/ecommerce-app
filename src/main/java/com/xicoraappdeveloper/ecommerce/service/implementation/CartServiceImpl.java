package com.xicoraappdeveloper.ecommerce.service.implementation;

import com.xicoraappdeveloper.ecommerce.common.errors.BadRequestException;
import com.xicoraappdeveloper.ecommerce.common.errors.ForbiddenAccessException;
import com.xicoraappdeveloper.ecommerce.common.errors.InventoryException;
import com.xicoraappdeveloper.ecommerce.common.errors.ResourcesNotFoundException;
import com.xicoraappdeveloper.ecommerce.entity.Cart;
import com.xicoraappdeveloper.ecommerce.entity.CartItem;
import com.xicoraappdeveloper.ecommerce.entity.Product;
import com.xicoraappdeveloper.ecommerce.model.dto.response.CartItemResponse;
import com.xicoraappdeveloper.ecommerce.repository.CartItemRepository;
import com.xicoraappdeveloper.ecommerce.repository.CartRepository;
import com.xicoraappdeveloper.ecommerce.repository.ProductRepository;
import com.xicoraappdeveloper.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {


    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void addItemToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .build();
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findByIdWithPessimisticLock(productId)
                .orElseThrow(
                        () -> new ResourcesNotFoundException("product with id " + productId + " is not found"));

        if (product.getUserId().equals(userId)) {
            throw new BadRequestException("Cannot add your own product to cart");
        }

        if (product.getStockQuantity() <= 0) {
            throw new InventoryException("Product stock is equal or below zero");
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(
                cart.getCartId(), productId);

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cartId(cart.getCartId())
                    .productId(productId)
                    .quantity(quantity)
                    .price(product.getPrice())
                    .build();
            cartItemRepository.save(newItem);
        }
    }

    @Override
    public void updateCartItemQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new ResourcesNotFoundException("Cart not found for user with id " + userId));

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(
                cart.getCartId(), productId);

        if (existingItemOpt.isEmpty()) {
            throw new ResourcesNotFoundException("Product " + productId + " is not yet added to cart");
        }

        CartItem item = existingItemOpt.get();
        if (!item.getCartId().equals(cart.getCartId())) {
            throw new ForbiddenAccessException("Cart item doesn't belong to user's cart");
        }

        if (quantity <= 0) {
            cartItemRepository.deleteById(item.getCartItemId());
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Transactional
    @Override
    public void removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new ResourcesNotFoundException("Cart not found for user with id " + userId));

        Optional<CartItem> existingItemOpt = cartItemRepository.findById(cartItemId);

        if (existingItemOpt.isEmpty()) {
            throw new ResourcesNotFoundException("Cart item not found");
        }

        CartItem item = existingItemOpt.get();

        if (!item.getCartId().equals(cart.getCartId())) {
            throw new ForbiddenAccessException("Cart item doesn't belong to user's cart");
        }

        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new ResourcesNotFoundException("Cart not found for user with id " + userId));
        cartItemRepository.deleteAllByCartId(cart.getCartId());
    }

    @Override
    public List<CartItemResponse> getCartItems(Long userId) {
        List<CartItem> cartItems = cartItemRepository.getUserCartItems(userId);
        if (cartItems.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));

        return cartItems.stream()
                .map(cartItem -> {
                    Product product = productMap.get(cartItem.getProductId());
                    if (product == null) {
                        throw new ResourcesNotFoundException(
                                "Product not found for ID: " + cartItem.getProductId());
                    }
                    return CartItemResponse.fromCartItemAndProduct(cartItem, product);
                })
                .toList();
    }
}
