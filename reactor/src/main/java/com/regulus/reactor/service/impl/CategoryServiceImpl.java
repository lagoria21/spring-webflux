package com.regulus.reactor.service.impl;

import com.regulus.reactor.documents.Category;
import com.regulus.reactor.dto.CategoryResponse;
import com.regulus.reactor.repository.CategoryRepository;
import com.regulus.reactor.service.CategoryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Mono<CategoryResponse> saveCategory(Category category) {
        return categoryRepository.save(category).map(c -> CategoryResponse.builder().name(c.getName()).build());
    }
}
