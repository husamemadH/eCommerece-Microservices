package com.user.user_service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @AllArgsConstructor
// @JsonPropertyOrder({ "id", "firstName", "lastName" , "email" ,
// "phoneNumber"}
@Document(collection = "users")
public class User {

  @Id
  private String id;

  private String firstName;

  private String lastName;

  @Indexed(unique = true)
  private String email;

  private String phoneNumber;

  private UserRole userRole = UserRole.CUSTOMER;

  private Address address;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}
