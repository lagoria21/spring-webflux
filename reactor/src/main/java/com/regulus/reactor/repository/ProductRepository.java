package com.regulus.reactor.repository;

import com.regulus.reactor.documents.Product;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    @Query("{ 'id': ?0 }")
    Mono<Product> findByIdProduct(String id);

    @Query("{ 'name': ?0 }")
    Mono<Product> findByNameProduct(String name);

}
