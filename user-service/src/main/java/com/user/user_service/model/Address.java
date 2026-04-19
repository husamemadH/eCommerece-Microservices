package com.user.user_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {

  private String country;

  private String state;

  private String zipcode;

}
