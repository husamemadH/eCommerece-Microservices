package com.order.order_service.dto;

import lombok.Data;

@Data
public class CartItemRequest {

  private String productId;

  private Integer quantity;
}
