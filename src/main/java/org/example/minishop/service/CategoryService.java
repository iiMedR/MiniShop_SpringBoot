package org.example.minishop.service;

import org.example.minishop.dto.category.CategoryResponse;
import org.example.minishop.dto.category.CreateCategoryRequest;
import org.example.minishop.exception.category.CategoryAlreadyExistException;
import org.example.minishop.model.Category;
import org.example.minishop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(),  category.getName());
    }

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        if(categoryRepository.existsByName(request.name())){
            throw new CategoryAlreadyExistException(request.name());
        }
        category.setName(request.name());
        categoryRepository.save(category);
        return toResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::toResponse).toList();
    }
}
