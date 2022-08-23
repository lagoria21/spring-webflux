package com.regulus.reactor.service;

import com.regulus.reactor.documents.Category;
import com.regulus.reactor.dto.CategoryResponse;
import reactor.core.publisher.Mono;

public interface CategoryService {
    Mono<CategoryResponse> saveCategory(Category category);

}
