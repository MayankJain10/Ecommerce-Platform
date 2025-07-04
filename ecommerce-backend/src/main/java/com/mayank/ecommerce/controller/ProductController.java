package com.mayank.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

import com.mayank.ecommerce.entity.Product;
import com.mayank.ecommerce.entity.dto.ProductDTO;
import com.mayank.ecommerce.exception.ProductNotFoundException;
import com.mayank.ecommerce.repository.ProductRepository;
import com.mayank.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "APIs for managing products in the ecommerce system")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductRepository productRepository;

    @Operation(summary = "Create a new product", description = "Only accessible by ADMIN. Adds a new product to the store.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product created successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - Admin only")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @Operation(summary = "Get all products", description = "Returns a list of all available products.")
    @ApiResponse(responseCode = "200", description = "Product list fetched successfully")
    @GetMapping("/allproducts")
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get product by ID", description = "Fetch a specific product using its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product fetched successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/allProducts/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with given id: " + id));
    }

    @Operation(summary = "Update product", description = "Only accessible by ADMIN. Updates a product by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - Admin only")
    })
    @PutMapping("/update/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProductById(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.updateProductById(id, productDTO);
    }

    @Operation(summary = "Delete product", description = "Only accessible by ADMIN. Deletes a product by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - Admin only")
    })
    @DeleteMapping("/delete/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @Operation(summary = "Search products", description = "Search for products using a keyword.")
    @ApiResponse(responseCode = "200", description = "Search results returned")
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> results = productService.searchProduct(keyword);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Sort products", description = "Sort products by name or price in ascending/descending order.")
    @ApiResponse(responseCode = "200", description = "Sorted product list returned")
    @GetMapping("/sorted")
    public ResponseEntity<List<Product>> getSortedProducts(
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        List<Product> products = productService.getSortedProducts(sortBy, direction);
        return ResponseEntity.ok(products);
    }
    
    @Operation(summary = "toggle-products", description = "toggle products by productID .")
    @ApiResponse(responseCode = "200", description = "toggle product, available or not!")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/toggle-product/{productId}")
    public ResponseEntity<String> toggleProduct(@PathVariable Long productId) {
        productService.toggleProductAvailability(productId);
        return ResponseEntity.ok("Product availability toggled.");
    }
    
 // ProductController.java
    @PostMapping("/admin/upload-image/{productId}")
    public ResponseEntity<String> uploadProductImage(@PathVariable Long productId,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = productService.saveProductImage(productId, file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }
    
    @PutMapping("/admin/product/image/{id}")
    public ResponseEntity<?> updateProductImage(@PathVariable Long id, @RequestParam("imageUrl") String imageUrl) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setImageUrl(imageUrl);
        productRepository.save(product);

        return ResponseEntity.ok("Image URL updated");
    }



}
