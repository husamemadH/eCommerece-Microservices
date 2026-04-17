package com.app.ecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
	
	private final ProductService productService;
	
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getProducts() {
		
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(productService.createProduct(productRequest));
	}
	
	@PutMapping("{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id ,@RequestBody ProductRequest productRequest) {
		
		 ProductResponse productResponse = productService.updateProduct(id, productRequest);
		 
		 return ResponseEntity.ok(productResponse);
				 
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id ) {
		
		productService.deleteProduct(id); 
				
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
		
		return ResponseEntity.ok(productService.searchProducts(keyword));
		
	}
}
