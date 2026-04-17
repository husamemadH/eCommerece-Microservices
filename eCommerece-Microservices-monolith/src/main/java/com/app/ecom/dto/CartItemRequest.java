package com.app.ecom.dto;

import com.app.ecom.model.Product;

import lombok.Data;

@Data
public class CartItemRequest {

	
	private Long productId;
	
	private Integer quantity;
}
