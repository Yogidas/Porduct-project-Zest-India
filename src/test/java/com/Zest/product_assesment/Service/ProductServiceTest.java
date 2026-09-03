package com.Zest.product_assesment.Service;

import com.Zest.product_assesment.Entity.Product;
import com.Zest.product_assesment.Exception.BadRequestException;
import com.Zest.product_assesment.Exception.ResourceNotFoundException;
import com.Zest.product_assesment.Repositories.ProductRepository;
import com.Zest.product_assesment.Service.ProductService;
import com.Zest.product_assesment.dto.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .productName("Enterprise License")
                .quantity(10)
                .price(499.99)
                .createdBy("admin")
                .build();
    }

    @Test
    void purchaseProduct_updatesStockSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.purchaseProduct(1L, 3);

        assertNotNull(response);
        assertEquals(7, product.getQuantity());
        verify(productRepository).save(product);
    }

    @Test
    void purchaseProduct_throwsException_whenStockIsInsufficient() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class, () -> productService.purchaseProduct(1L, 20));
        verify(productRepository, never()).save(any());
    }

    @Test
    void getProductById_throwsException_whenProductMissing() {
        when(productRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(42L));
    }
}