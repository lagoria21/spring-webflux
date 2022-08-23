package com.regulus.reactor.service.impl;

import com.regulus.reactor.documents.Category;
import com.regulus.reactor.documents.Product;
import com.regulus.reactor.dto.CategoryResponse;
import com.regulus.reactor.dto.ProductRequest;
import com.regulus.reactor.dto.ProductResponse;
import com.regulus.reactor.repository.ProductRepository;
import com.regulus.reactor.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<List<ProductResponse>> getAllProduct() {
        return productRepository.findAll()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .createAt(product.getCreateAt())
                        .category(CategoryResponse.builder().id(product.getCategory().getId()).name(product.getCategory().getName()).build())
                        .build()).collectList();
    }

    public Mono<ProductResponse> getProductById(String id) {
        return productRepository.findById(id).map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .createAt(product.getCreateAt())
                .category(CategoryResponse.builder().id(product.getCategory().getId()).name(product.getCategory().getName()).build())
                .build());
    }

    public Mono<ProductResponse> saveProduct(ProductRequest request) {
        return productRepository.save(Product.builder().name(request.getName()).price(request.getPrice())
                        .createAt(new Date()).category(Category.builder().name(request.getCategory().getName()).build()).build())
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .createAt(product.getCreateAt())
                        .category(CategoryResponse.builder().id(product.getCategory().getId()).name(product.getCategory().getName()).build())
                        .build());
    }

    public Mono<ProductResponse> saveProductV2(Mono<ProductRequest> request) {
        return request.flatMap(produc -> productRepository.save(Product.builder().name(produc.getName())
                .price(produc.getPrice()).createAt(new Date()).category(Category.builder().name(produc.getCategory().getName()).build()).build()
        )).map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .createAt(product.getCreateAt())
                .category(CategoryResponse.builder().id(product.getCategory().getId()).name(product.getCategory().getName()).build())
                .build());
    }

    public Mono<ProductResponse> updateProduct(ProductRequest request, String id) {
        return productRepository.findByIdProduct(id).flatMap(product ->
             productRepository.save(Product.builder().id(id).name(request.getName()).price(request.getPrice())
                    .createAt(new Date())
                    .category(Category.builder().id(product.getCategory().getId()).name(product.getCategory().getName()).build())
                    .build())
        ).map(pro -> ProductResponse.builder()
                .id(pro.getId())
                .name(pro.getName())
                .price(pro.getPrice())
                .createAt(pro.getCreateAt())
                .category(CategoryResponse.builder().id(pro.getCategory().getId()).name(pro.getCategory().getName()).build()).build());
    }

    public Mono<Void> removeProduct(String id) {
        return productRepository.deleteById(id);
    }

    public Mono<Product> findByName(String name) {
        return productRepository.findByNameProduct(name);
    }
}
