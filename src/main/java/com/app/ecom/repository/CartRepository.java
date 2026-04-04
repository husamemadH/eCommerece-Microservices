package com.app.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;

public interface CartRepository extends JpaRepository<CartItem , Long> {

	  CartItem  findByUserAndProduct(User user, Product product);

	  void deleteByUserAndProduct(User user, Product product);

	  List<CartItem> findByUser(User user);

}
