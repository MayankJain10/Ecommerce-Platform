package com.mayank.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mayank.ecommerce.entity.Product;
import com.mayank.ecommerce.entity.dto.ProductDTO;
import com.mayank.ecommerce.exception.ProductNotFoundException;
import com.mayank.ecommerce.repository.ProductRepository;

import org.springframework.data.domain.Sort;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	public Product createProduct(ProductDTO productDTO) {
		Product prod = new Product();
		
		prod.setName(productDTO.getName());
		prod.setDescription(productDTO.getDescription());
		prod.setPrice(productDTO.getPrice());
		prod.setQuantity(productDTO.getQuantity());
		prod.setImageUrl(productDTO.getImageUrl());
		
		return productRepository.save(prod);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	public Product updateProductById(Long id, ProductDTO productDTO) {
		Product prod = productRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Product not found with given id: " + id));
		
		prod.setName(productDTO.getName());
		prod.setDescription(productDTO.getDescription());
		prod.setPrice(productDTO.getPrice());
		prod.setQuantity(productDTO.getQuantity());
		prod.setImageUrl(productDTO.getImageUrl());
		return productRepository.save(prod);
	}

	public void deleteProductById(Long id) {
		productRepository.deleteById(id);		
	}

	public List<Product> searchProduct(String keyword) {
		return productRepository.findByNameContainingIgnoreCase(keyword);
	}

	public List<Product> getSortedProducts(String sortBy, String direction) {
	    Sort sort = direction.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    return productRepository.findAll(sort);
	}

	public void toggleProductAvailability(Long productId) {
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

	    product.setAvailable(!product.isAvailable());
	    productRepository.save(product);
	}

	public String saveProductImage(Long productId, MultipartFile file) throws IOException {
	    Product product = productRepository.findById(productId)
	        .orElseThrow(() -> new RuntimeException("Product not found"));

	    String uploadDir = "uploaded-images/";
	    File directory = new File(uploadDir);
	    if (!directory.exists()) directory.mkdirs();

	    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	    Path filePath = Paths.get(uploadDir + fileName);
	    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	    // Save image URL to product
	    String imageUrl = "/images/" + fileName;
	    product.setImageUrl(imageUrl);
	    productRepository.save(product);

	    return imageUrl;
	}



}
