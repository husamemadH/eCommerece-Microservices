package com.app.ecom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.exception.ResourceNotFoundException;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

 
@Service
@RequiredArgsConstructor

public class UserService {
	private final UserRepository userRepository;
	

	
	public List<UserResponse> getAllUsers() {
		
		return userRepository.findAll()
				.stream()
				.map(this::mapUserToUserResponse)
				.collect(Collectors.toList());
		
	}
	
	public UserResponse getSingleUser(Long id) {
		
		return userRepository.findById(id)
				.map(this::mapUserToUserResponse)
				.orElseThrow(() -> new ResourceNotFoundException("user not found"));
	}
	
	public void addUser(UserRequest userRequest) {
		User user = new User();
		
		updateUserFromRequest(user , userRequest);
		
		 userRepository.save(user);
	}
	
	private void updateUserFromRequest(User user, UserRequest userRequest) {
		 user.setFirstName(userRequest.getFirstName());
		 user.setLastName(userRequest.getLastName());
		 user.setEmail(userRequest.getEmail());
		 user.setPhoneNumber(userRequest.getPhoneNumber());
		 if(userRequest.getAddress() != null) {
			 Address address = new Address();
			 address.setCountry(userRequest.getAddress().getCountry());
			 address.setState(userRequest.getAddress().getState());
			 address.setZipcode(userRequest.getAddress().getZipcode());
			 
			 user.setAddress(address);
		 }
		 
		
	}

	public UserResponse updateUser(Long id , UserRequest updatedUserRequest) {
		 User user = userRepository.findById(id)
		   .orElseThrow(() -> new ResourceNotFoundException("user not found"));
		   
		    updateUserFromRequest(user , updatedUserRequest);
		   
		 User savedUser = userRepository.save(user);
		 
		 return mapUserToUserResponse(savedUser);
	} 
	
	public UserResponse mapUserToUserResponse(User user) {
		UserResponse response = new UserResponse();
		
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setEmail(user.getEmail());
		response.setPhoneNumber(user.getPhoneNumber());
		response.setUserRole(user.getUserRole());
		response.setId(String.valueOf(user.getId()));
		
		if(user.getAddress() != null) {
			AddressDTO address = new AddressDTO();
			address.setCountry(user.getAddress().getCountry());
			address.setState(user.getAddress().getState());
			address.setZipcode(user.getAddress().getZipcode());
			
			response.setAddress(address);
		}
		
		
		return response;
	}
}
 