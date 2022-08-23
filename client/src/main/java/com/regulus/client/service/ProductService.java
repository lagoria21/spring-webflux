package com.regulus.client.service;

import com.regulus.client.dto.Product;
import com.regulus.client.dto.ProductRequestAdd;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    public Flux<Product> findAll();

    public Mono<Product> findById(String id);

    public Mono<Product> save(ProductRequestAdd product);

    public Mono<Void> delete(String id);

}
