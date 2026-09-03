package com.Zest.product_assesment.Service;

import com.Zest.product_assesment.Entity.Product;
import com.Zest.product_assesment.Exception.BadRequestException;
import com.Zest.product_assesment.Exception.ResourceNotFoundException;
import com.Zest.product_assesment.Repositories.ProductRepository;
import com.Zest.product_assesment.dto.ProductRequestDTO;
import com.Zest.product_assesment.dto.ProductResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToDTO);
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Product product = Product.builder()
                .productName(requestDTO.getProductName())
                .quantity(requestDTO.getQuantity())
                .price(requestDTO.getPrice())
                .createdBy(currentUsername)
                .build();

        return mapToDTO(productRepository.save(product));
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        product.setProductName(requestDTO.getProductName());
        product.setQuantity(requestDTO.getQuantity());
        product.setPrice(requestDTO.getPrice());
        product.setModifiedBy(currentUsername);

        return mapToDTO(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductResponseDTO purchaseProduct(Long id, int quantityToBuy) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getQuantity() < quantityToBuy) {
            throw new BadRequestException("Insufficient stock. Available quantity: " + product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - quantityToBuy);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    private ProductResponseDTO mapToDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .createdBy(product.getCreatedBy())
                .createdOn(product.getCreatedOn())
                .modifiedBy(product.getModifiedBy())
                .modifiedOn(product.getModifiedOn())
                .build();
    }
}