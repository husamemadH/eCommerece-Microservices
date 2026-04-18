package com.order.order_service.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.order.order_service.dto.OrderItemDTO;
import com.order.order_service.dto.OrderResponse;
import com.order.order_service.exception.EmptyCartException;
import com.order.order_service.model.CartItem;
import com.order.order_service.model.Order;
import com.order.order_service.model.OrderItem;
import com.order.order_service.model.OrderStatus;
import com.order.order_service.repository.OrderRepository;
import com.order.order_service.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final CartRepository cartRepository;

  public OrderResponse createOrder(String userId) {

    List<CartItem> cartItems = cartRepository.findByUserId(userId);

    if (cartItems.isEmpty()) {
      throw new EmptyCartException("Order cant be created cart is empty");
    }

    BigDecimal totalPrice = BigDecimal.ZERO;

    for (int i = 0; i < cartItems.size(); i++) {
      totalPrice = totalPrice.add(cartItems.get(i).getPrice());
    }

    Order order = new Order();
    order.setStatus(OrderStatus.CONFIRMED);
    order.setTotalAmount(totalPrice);
    order.setUserId(userId);
    List<OrderItem> orderItems = cartItems.stream()
        .map(item -> new OrderItem(null, item.getProductId(), item.getQuantity(), item.getPrice(), order)).toList();
    order.setItems(orderItems);

    Order savedOrder = orderRepository.save(order);

    cartRepository.deleteByUserId(userId);

    return mapToOrderResponse(savedOrder);
  }

  private OrderResponse mapToOrderResponse(Order order) {
    List<OrderItemDTO> itemDtos = order.getItems().stream()
        .map(item -> new OrderItemDTO(
            item.getId(),
            item.getProductId(),
            item.getQuantity(),
            item.getPrice()))
        .toList();

    return new OrderResponse(
        order.getId(),
        order.getTotalAmount(),
        order.getStatus(),
        itemDtos,
        order.getCreatedAt());
  }

}
