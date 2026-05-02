package com.order.order_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.order_service.client.ProductClient;
import com.order.order_service.client.UserClient;
import com.order.order_service.dto.CartItemRequest;
import com.order.order_service.dto.CartItemResponse;
import com.order.order_service.dto.ProductResponse;
import com.order.order_service.dto.UserResponse;
import com.order.order_service.model.CartItem;
import com.order.order_service.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

  private final CartRepository cartRepository;

  private final ProductClient productClient;

  private final UserClient userClient;

  public void addToCart(String userId, CartItemRequest request) {

    ProductResponse productResponse = productClient.getProductDetails(request.getProductId());
    UserResponse userResponse = userClient.getUserDetails(userId);

    CartItem cartItem = cartRepository.findByUserIdAndProductId(userId, productResponse.getId());

    if (cartItem != null) {
      cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
    } else {
      cartItem = new CartItem();
      cartItem.setUserId(userId);
      cartItem.setProductId(request.getProductId());
      cartItem.setQuantity(request.getQuantity());
    }

    BigDecimal totalPrice = productResponse.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    cartItem.setPrice(totalPrice);

    cartRepository.save(cartItem);
  }

  public void removeFromCart(String userId, Long productId) {
    cartRepository.deleteByUserIdAndProductId(userId, String.valueOf(productId));
  }

  public List<CartItemResponse> getAllCartItems(String userId) {
    return cartRepository.findByUserId(userId)
        .stream()
        .map(this::mapCartItemToResponse)
        .collect(Collectors.toList());
  }

  private CartItemResponse mapCartItemToResponse(CartItem item) {
    CartItemResponse response = new CartItemResponse();
    response.setId(item.getId());
    response.setUserId(item.getUserId());
    response.setProductId(item.getProductId());
    response.setQuantity(item.getQuantity());
    response.setPrice(item.getPrice());
    return response;
  }

}
