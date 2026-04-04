package com.app.ecom.dto;

import java.math.BigDecimal;

import com.app.ecom.model.Product;
import com.app.ecom.model.User;

import lombok.Data;

@Data
public class CartItemResponse {
	
	private Long id;

	private User user;

	private Product product;

	private Integer quantity;

	private BigDecimal price;

}
