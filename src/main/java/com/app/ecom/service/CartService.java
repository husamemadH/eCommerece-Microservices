package com.app.ecom.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
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
	
	public boolean addToCart(String userId, CartItemRequest request) {
		Optional<Product> productOpt = productRepository.findById(request.getProductId());
		
		if(productOpt.isEmpty())
			return false;
		Product product = productOpt.get();
		
		if(! (product.getStockQuantity() > 0)) 
			return false;
		
		Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
		if(userOpt.isEmpty())
			return false;
		
		User user = userOpt.get();
		
		 CartItem  existingCartItem = cartRepository.findByUserAndProduct(user , product);
		
		if(existingCartItem != null) {
		   existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
		   existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
		   cartRepository.save(existingCartItem);
		}
		else {
			CartItem cartItem = new CartItem();
			cartItem.setUser(user);
			cartItem.setProduct(product);
			cartItem.setQuantity(request.getQuantity());
			cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
			 
			cartRepository.save(cartItem);
		}
		
		return true;	}

	public boolean removeFromCart(String userId, Long productId) {
		 Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
		 Optional<Product> productOpt  = productRepository.findById(productId);
		 
		 if(userOpt.isPresent() && productOpt.isPresent()) {
			 cartRepository.deleteByUserAndProduct(userOpt.get() , productOpt.get());
			 return true;
			 
		 }
		 
		 return false;
		 
	}
	
	public List<CartItemResponse> getAllCartItems(String userId) {
		
		
		
		Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
		
		if(userOpt.isEmpty()) return Collections.emptyList();
	
		return cartRepository.findByUser(userOpt.get())
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
