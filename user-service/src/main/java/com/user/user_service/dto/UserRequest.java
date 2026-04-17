package com.user.user_service.dto;

import com.user.user_service.model.Address;

import lombok.Data;

@Data
public class UserRequest {

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  private Address address;

  public UserRequest() {
  }
}
