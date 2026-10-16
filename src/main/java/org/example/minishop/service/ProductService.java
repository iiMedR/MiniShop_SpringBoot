package org.example.minishop.service;

import org.example.minishop.dto.product.CreateProductRequest;
import org.example.minishop.dto.product.ProductResponse;
import org.example.minishop.exception.category.CategoryNotFoundException;
import org.example.minishop.model.Category;
import org.example.minishop.model.Product;
import org.example.minishop.repository.CategoryRepository;
import org.example.minishop.repository.ProductRepository;
import org.springframework.stereotype.Service;

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
}
