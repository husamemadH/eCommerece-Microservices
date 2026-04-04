package com.app.ecom.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.exception.InsufficientStockException;
import com.app.ecom.exception.ResourceNotFoundException;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	
	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	
	public void addToCart(String userId, CartItemRequest request) {
		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("product not found with id: " + request.getProductId()));
	
		if(!(product.getStockQuantity() > 0)) {
			
			throw new InsufficientStockException("Product is out of stock");
		}
			 
		User user = userRepository.findById(Long.valueOf(userId))
				.orElseThrow(() -> new ResourceNotFoundException("user not found with id : " + userId));
				
		 CartItem  cartItem = cartRepository.findByUserAndProduct(user , product);
		
		if(cartItem != null) {
			cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
		}
		else {
			cartItem = new CartItem();
	        cartItem.setUser(user);
	        cartItem.setProduct(product);
	        cartItem.setQuantity(request.getQuantity());
		}
		
		BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
	    cartItem.setPrice(totalPrice);
	    
	    cartRepository.save(cartItem);
		
	}

	public void removeFromCart(String userId, Long productId) {
		 User user  = userRepository.findById(Long.valueOf(userId))
				 .orElseThrow(() -> new ResourceNotFoundException("user not found with id : " + userId));
		 Product product  = productRepository.findById(productId)
				 .orElseThrow(() -> new ResourceNotFoundException("product not found with id: " + productId));
	 
		 cartRepository.deleteByUserAndProduct(user , product);
		 
	}
	
	public List<CartItemResponse> getAllCartItems(String userId) {
		 
		 User user  = userRepository.findById(Long.valueOf(userId))
				 .orElseThrow(() -> new ResourceNotFoundException("user not found with id : " + userId));
	
		return cartRepository.findByUser(user)
				.stream()
				.map(this::mapCartItemToResponse)
				.collect(Collectors.toList()); 
	}
	
	 
	
	public CartItemResponse mapCartItemToResponse(CartItem item) {
		CartItemResponse response = new CartItemResponse();
		
		response.setId(item.getId());
		response.setPrice(item.getPrice());
		response.setProduct(item.getProduct());
		response.setQuantity(item.getQuantity());
	 

		return response;
	}
	
}
