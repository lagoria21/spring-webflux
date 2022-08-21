package com.regulus.reactor.service;

import com.regulus.reactor.documents.Category;
import com.regulus.reactor.dto.CategoryResponse;
import com.regulus.reactor.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Mono<CategoryResponse> saveCategory(Category category) {
        return categoryRepository.save(category).map(category1 -> CategoryResponse.builder().build());
    }
}
