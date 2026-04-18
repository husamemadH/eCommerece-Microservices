package com.order.order_service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemResponse {

  private Long id;

  private String userId;

  private String productId;

  private Integer quantity;

  private BigDecimal price;

}
