package com.app.ecom.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.exception.EmptyCartException;
import com.app.ecom.exception.ResourceNotFoundException;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Order;
import com.app.ecom.model.OrderItem;
import com.app.ecom.model.OrderStatus;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartRepository;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	
	public OrderResponse createOrder(String userId) {
		
		User user = userRepository.findById(Long.valueOf(userId))
				.orElseThrow(() -> new ResourceNotFoundException("user not found"));
		
		List<CartItem> cartItems = cartRepository.findByUser(user);
		
		 if(cartItems.isEmpty()) {
			 throw new EmptyCartException("Order cant be created cart is empty");
		 }
		 
		 BigDecimal totalPrice = BigDecimal.ZERO;
		 
		 for(int i = 0 ; i < cartItems.size() ; i++) {
		 
			totalPrice = totalPrice.add(cartItems.get(i).getPrice()); 
			
		 }
		 
		 Order order = new Order();
		 order.setStatus(OrderStatus.CONFIRMED);
		 order.setTotalAmount(totalPrice);
		 order.setUser(user);
		 List<OrderItem> orderItems = cartItems.stream()
				 .map(item -> new OrderItem(null , item.getProduct() , item.getQuantity() , item.getPrice() , order)).toList();
		 order.setItems(orderItems);
		 
		 Order savedOrder = orderRepository.save(order);
		 
		 cartRepository.deleteByUser(user);
		
		 return mapToOrderResponse(savedOrder);
	}
	
	private OrderResponse mapToOrderResponse(Order order) {
	    // Map the items first
	    List<OrderItemDTO> itemDtos = order.getItems().stream()
	            .map(item -> new OrderItemDTO(
	                item.getId(),
	                item.getProduct().getId(),
	                item.getQuantity(),
	                item.getPrice()
	               
	            ))
	            .toList();

	    // Now pass the list into the response
	    return new OrderResponse(
	        order.getId(),
	        order.getTotalAmount(),
	        order.getStatus(),
	        itemDtos,
	        order.getCreatedAt()
	    );
	}
		
		
		
	}
 
