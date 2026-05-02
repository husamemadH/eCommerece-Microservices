
package com.order.order_service.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.order.order_service.dto.UserResponse;

@HttpExchange
public interface UserClient {

  @GetExchange("api/users/{id}")

  UserResponse getUserDetails(@PathVariable String id);
}
