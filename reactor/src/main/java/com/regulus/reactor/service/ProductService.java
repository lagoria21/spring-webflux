package com.regulus.reactor.service;

import com.regulus.reactor.dto.ProductRequest;
import com.regulus.reactor.dto.ProductResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {

    public Mono<List<ProductResponse>> getAllProduct();

    public Mono<ProductResponse> getProductById(String id);

    public Mono<ProductResponse> saveProduct(ProductRequest request);

    public Mono<ProductResponse> saveProductV2(Mono<ProductRequest> request);

    public Mono<ProductResponse> updateProduct(ProductRequest request, String id);

    public Mono<Void> removeProduct( String id);
}
