package com.order.order_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.order.order_service.dto.OrderResponse;
import com.order.order_service.exception.EmptyCartException;
import com.order.order_service.model.CartItem;
import com.order.order_service.model.Order;
import com.order.order_service.model.OrderStatus;
import com.order.order_service.repository.CartRepository;
import com.order.order_service.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private CartRepository cartRepository;

  @InjectMocks
  private OrderService orderService;

  @Test
  void createOrder_throwsEmptyCartException_whenCartIsEmpty() {
    when(cartRepository.findByUserId("user-1")).thenReturn(Collections.emptyList());

    assertThatThrownBy(() -> orderService.createOrder("user-1"))
        .isInstanceOf(EmptyCartException.class)
        .hasMessageContaining("cart is empty");
  }

  @Test
  void createOrder_returnsOrderResponse_withCorrectTotal() {
    CartItem item1 = new CartItem();
    item1.setUserId("user-1");
    item1.setProductId("prod-A");
    item1.setQuantity(2);
    item1.setPrice(new BigDecimal("50.00"));

    CartItem item2 = new CartItem();
    item2.setUserId("user-1");
    item2.setProductId("prod-B");
    item2.setQuantity(1);
    item2.setPrice(new BigDecimal("30.00"));

    when(cartRepository.findByUserId("user-1")).thenReturn(List.of(item1, item2));

    Order savedOrder = new Order();
    savedOrder.setId(1L);
    savedOrder.setUserId("user-1");
    savedOrder.setTotalAmount(new BigDecimal("80.00"));
    savedOrder.setStatus(OrderStatus.CONFIRMED);
    savedOrder.setItems(Collections.emptyList());

    when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

    OrderResponse response = orderService.createOrder("user-1");

    assertThat(response.getTotalAmount()).isEqualByComparingTo("80.00");
    assertThat(response.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    verify(cartRepository).deleteByUserId("user-1");
  }

  @Test
  void createOrder_clearsCartAfterOrderIsCreated() {
    CartItem item = new CartItem();
    item.setUserId("user-1");
    item.setProductId("prod-A");
    item.setQuantity(1);
    item.setPrice(new BigDecimal("25.00"));

    when(cartRepository.findByUserId("user-1")).thenReturn(List.of(item));

    Order savedOrder = new Order();
    savedOrder.setId(2L);
    savedOrder.setUserId("user-1");
    savedOrder.setTotalAmount(new BigDecimal("25.00"));
    savedOrder.setStatus(OrderStatus.CONFIRMED);
    savedOrder.setItems(Collections.emptyList());

    when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

    orderService.createOrder("user-1");

    verify(cartRepository).deleteByUserId("user-1");
  }

}
