package com.xicoraappdeveloper.ecommerce.controller;

import com.xicoraappdeveloper.ecommerce.model.UserInfo;
import com.xicoraappdeveloper.ecommerce.model.dto.request.AddToCartRequest;
import com.xicoraappdeveloper.ecommerce.model.dto.request.UpdateCartItemRequest;
import com.xicoraappdeveloper.ecommerce.model.dto.response.CartItemResponse;
import com.xicoraappdeveloper.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("carts")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // POST /api/v1/carts/items
    @PostMapping("/items")
    public ResponseEntity<Void> addItemToCart(@Valid @RequestBody AddToCartRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        cartService.addItemToCart(userInfo.getUser().getUserId(),
                request.getProductId(),
                request.getQuantity());

        return ResponseEntity.ok().build();
    }

    // PUT /api/v1/carts/items
    @PutMapping("/items")
    public ResponseEntity<Void> updateCartItemQuantity(
            @Valid @RequestBody UpdateCartItemRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        cartService.updateCartItemQuantity(userInfo.getUser().getUserId(),
                request.getProductId(),
                request.getQuantity());

        return ResponseEntity.ok().build();
    }

    // DELETE /api/v1/carts/items/:d
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("id") Long cartItemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        cartService.removeItemFromCart(userInfo.getUser().getUserId(), cartItemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        List<CartItemResponse> items = cartService.getCartItems(userInfo.getUser().getUserId());
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        cartService.clearCart(userInfo.getUser().getUserId());
        return ResponseEntity.noContent().build();
    }
}