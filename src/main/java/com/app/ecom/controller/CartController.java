package com.app.ecom.controller;

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

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
	
	private final CartService cartService;
	
	@GetMapping
	public ResponseEntity<List<CartItemResponse>> getAllCartItems(@RequestHeader("X-User-ID") String userId) {
		
		List<CartItemResponse> responseList = cartService.getAllCartItems( userId );
		
		return responseList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(responseList);
		
	}
	
	@PostMapping
	public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId, @RequestBody CartItemRequest request) {
		
 
		return cartService.addToCart(userId , request)  ? 
				ResponseEntity.status(HttpStatus.CREATED).build() :
			    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product out of stockor User is not found or product not found");
 
	} 
	
	@DeleteMapping("/items/{productId}")
	public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId , @PathVariable Long productId) {
		
		boolean deleted = cartService.removeFromCart(userId , productId);
		
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
		
		
	}


}
