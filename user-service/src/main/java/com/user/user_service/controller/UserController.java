package com.user.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.user_service.dto.UserRequest;
import com.user.user_service.dto.UserResponse;
import com.user.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/api/users")
  public ResponseEntity<List<UserResponse>> getAllUsers() {

    return ResponseEntity.ok(userService.getAllUsers());

  }

  @GetMapping("/api/users/{id}")
  public ResponseEntity<UserResponse> getSinglelUser(@PathVariable Long id) {

    UserResponse userResponse = userService.getSingleUser(id);

    return ResponseEntity.ok(userResponse);

  }

  @PostMapping("/api/users")
  public ResponseEntity<Void> addUser(@RequestBody UserRequest userRequest) {

    userService.addUser(userRequest);

    return ResponseEntity.status(HttpStatus.CREATED).build();

  }

  @PutMapping("/api/users/{id}")

  public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {

    UserResponse updatedUserResponse = userService.updateUser(id, userRequest);

    return ResponseEntity.ok(updatedUserResponse);

  }
}
