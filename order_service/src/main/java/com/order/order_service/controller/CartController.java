package com.order.order_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.order_service.dto.CartItemRequest;
import com.order.order_service.dto.CartItemResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @GetMapping
  public ResponseEntity<List<CartItemResponse>> getAllCartItems(@RequestHeader("X-User-ID") String userId) {

    List<CartItemResponse> responseList = cartService.getAllCartItems(userId);

    return ResponseEntity.ok().body(responseList);

  }

  @PostMapping
  public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
      @RequestBody CartItemRequest request) {

    cartService.addToCart(userId, request);

    return ResponseEntity.status(HttpStatus.CREATED).body("Item added to cart successfully");
  }

  @DeleteMapping("/items/{productId}")
  public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable Long productId) {

    cartService.removeFromCart(userId, productId);

    return ResponseEntity.noContent().build();

  }

}
