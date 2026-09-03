package com.Zest.product_assesment.Controller;

import com.Zest.product_assesment.Service.ProductService;
import com.Zest.product_assesment.dto.ProductRequestDTO;
import com.Zest.product_assesment.dto.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Management", description = "APIs for product catalog inventory, updates, and purchases")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieves a paginated list of all products. Accessible by USER and ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access token")
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(page, size)));
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access token")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Create product", description = "Creates a new product entry. Restricted strictly to ADMIN users.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or validation error"),
            @ApiResponse(responseCode = "403", description = "Access denied (Admin role required)")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        return new ResponseEntity<>(productService.createProduct(requestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Update product", description = "Updates an existing product by ID. Restricted strictly to ADMIN users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Access denied (Admin role required)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO requestDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, requestDTO));
    }

    @Operation(summary = "Purchase product", description = "Purchases a specified quantity of a product, deducting stock")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Purchase successful, stock updated"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock available"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{id}/purchase")
    public ResponseEntity<ProductResponseDTO> purchaseProduct(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int quantity) {
        return ResponseEntity.ok(productService.purchaseProduct(id, quantity));
    }

    @Operation(summary = "Delete product", description = "Deletes a product by ID. Restricted strictly to ADMIN users.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Access denied (Admin role required)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}