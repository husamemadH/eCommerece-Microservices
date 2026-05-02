package com.order.order_service.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.order.order_service.dto.ProductResponse;

@HttpExchange
public interface ProductClient {

  @GetExchange("/api/products/{id}")
  ProductResponse getProductDetails(@PathVariable String id);

}
