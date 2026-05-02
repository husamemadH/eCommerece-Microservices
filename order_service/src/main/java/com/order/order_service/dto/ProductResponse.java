package com.order.order_service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductResponse {

  private String id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer stockQuantity;
  private String category;
  private String imageUrl;
  private Boolean active = true;

}
