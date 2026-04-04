package com.app.ecom.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.exception.ResourceNotFoundException;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	public final ProductRepository productRepository;
		
	public ProductResponse createProduct(ProductRequest productRequest) {
		
		 Product product = new Product();
		
		 updateProductFromRequest(product , productRequest);
		 
		 Product savedProduct = productRepository.save(product);
		 
		 return mapProductToProductResponse(savedProduct);
	 
	}
	
	public void updateProductFromRequest(Product product , ProductRequest productRequest) {
			 
			
		if (productRequest.getName() != null) {
	        product.setName(productRequest.getName());
	    }
	    if (productRequest.getCategory() != null) {
	        product.setCategory(productRequest.getCategory());
	    }
	    if (productRequest.getDescription() != null) {
	        product.setDescription(productRequest.getDescription());
	    }
	    if (productRequest.getImageUrl() != null) {
	        product.setImageUrl(productRequest.getImageUrl());
	    }
	    if (productRequest.getStockQuantity() != null) {
	        product.setStockQuantity(productRequest.getStockQuantity());
	    }
	    if (productRequest.getPrice() != null) {
	        product.setPrice(productRequest.getPrice());
	    }
			
			
	}
	
	public ProductResponse mapProductToProductResponse(Product product) {
		
		ProductResponse productResponse = new ProductResponse();
		
		productResponse.setId(product.getId());
		productResponse.setName(product.getName());
		productResponse.setCategory(product.getCategory());
		productResponse.setDescription(product.getDescription());
		productResponse.setImageUrl(product.getImageUrl());
		productResponse.setStockQuantity(product.getStockQuantity());
		productResponse.setPrice(product.getPrice());
		productResponse.setActive(product.getActive());
		
		return productResponse;
	 
		
	}

	public ProductResponse updateProduct(Long id , ProductRequest productRequest) {
		
		/* return productRepository.findById(id).map(existingProduct 
				 -> {updateProductFromRequest(existingProduct , productRequest);
		  			 Product savedProduct = productRepository.save(existingProduct);
		  			 return mapProductToProductResponse(savedProduct);}); */
		Product product =  productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		
		updateProductFromRequest(product , productRequest);
		
		Product savedProduct = productRepository.save(product);
		
		return mapProductToProductResponse(savedProduct);
		 
	 
	}

	public List<ProductResponse> getAllProducts() {
	 
		return productRepository.findByActiveTrue().stream()
				.map(this::mapProductToProductResponse)
				.collect(Collectors.toList());
 
	}

	public void deleteProduct(Long id) {
		/* return productRepository.findById(id)
				 .map(product -> { product.setActive(false); 
				 	  productRepository.save(product);
					  return true;	})
				 .orElse(false);*/
		
		Product product = productRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("product not found"));
		
		product.setActive(false);
		
		productRepository.save(product);
		
	}

	public List<ProductResponse> searchProducts(String keyword) {
		 
		return productRepository.searchProducts(keyword)
				.stream()
				.map(this::mapProductToProductResponse)
				.collect(Collectors.toList());
	}

}
