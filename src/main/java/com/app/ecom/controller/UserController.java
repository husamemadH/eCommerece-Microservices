package com.app.ecom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.User;
import com.app.ecom.service.UserService;

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
	public  ResponseEntity<UserResponse>  getSinglelUsers(@PathVariable Long id) {
		
		 return userService.getSingleUser(id)
				 .map(user -> ResponseEntity.ok(user))
				 .orElse(ResponseEntity.notFound().build());
		 
		
		 
	}
	
	@PostMapping("/api/users")
	public void addUser(@RequestBody UserRequest userRequest) {
		
		
		userService.addUser(userRequest);
	
	}
	
	@PutMapping("/api/users/{id}")
	
	public ResponseEntity<UserRequest> updateUser(@PathVariable Long id , @RequestBody UserRequest userRequest) {
		
		return userService.updateUser(id , userRequest) ? ResponseEntity.ok(userRequest) : ResponseEntity.notFound().build();
		
	}
}
