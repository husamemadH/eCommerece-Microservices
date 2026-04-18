package com.order.order_service.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.order.order_service.dto.OrderResponse;
import com.order.order_service.exception.EmptyCartException;
import com.order.order_service.model.OrderStatus;
import com.order.order_service.service.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private OrderService orderService;

  @Test
  void createOrder_returns201_withOrderResponse() throws Exception {
    OrderResponse response = new OrderResponse(
        1L,
        new BigDecimal("80.00"),
        OrderStatus.CONFIRMED,
        Collections.emptyList(),
        LocalDateTime.now());

    when(orderService.createOrder("user-1")).thenReturn(response);

    mockMvc.perform(post("/api/orders")
        .header("X-User-ID", "user-1"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.status").value("CONFIRMED"))
        .andExpect(jsonPath("$.totalAmount").value(80.00));
  }

  @Test
  void createOrder_returns400_whenCartIsEmpty() throws Exception {
    when(orderService.createOrder("user-1"))
        .thenThrow(new EmptyCartException("Order cant be created cart is empty"));

    mockMvc.perform(post("/api/orders")
        .header("X-User-ID", "user-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Order cant be created cart is empty"));
  }

  @Test
  void createOrder_returns400_whenUserIdHeaderIsMissing() throws Exception {
    mockMvc.perform(post("/api/orders"))
        .andExpect(status().isBadRequest());
  }

}
