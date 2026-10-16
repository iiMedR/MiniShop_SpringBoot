package org.example.minishop.service;

import jakarta.transaction.Transactional;
import org.example.minishop.dto.product.CreateProductRequest;
import org.example.minishop.dto.product.ProductResponse;
import org.example.minishop.dto.product.UpdateProductRequest;
import org.example.minishop.exception.category.CategoryNotFoundException;
import org.example.minishop.exception.product.ProductNotFoundException;
import org.example.minishop.model.Category;
import org.example.minishop.model.Product;
import org.example.minishop.repository.CategoryRepository;
import org.example.minishop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getCategory().getId());
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));
        product.setCategory(category);
        productRepository.save(product);
        return toResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::toResponse).toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        if(request.name() != null) {
            product.setName(request.name());
        }
        if(request.price() != null) {
            product.setPrice(request.price());
        }
        if(request.stock() != null) {
            product.setStock(request.stock());
        }
        return toResponse(product);
    }

    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }
}
