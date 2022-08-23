package com.regulus.client.service.impl;

import com.regulus.client.dto.Product;
import com.regulus.client.dto.ProductRequestAdd;
import com.regulus.client.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private WebClient.Builder client;

    @Override
    public Flux<Product> findAll() {
        return client.build().get().accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Product.class));
    }

    @Override
    public Mono<Product> findById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return client.build().get().uri("/{id}", params).accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Product.class));
    }

    @Override
    public Mono<Product> save(ProductRequestAdd product) {
        return client.build().post().uri("/add").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(product))
                .retrieve()
                .bodyToMono(Product.class);
    }

    @Override
    public Mono<Void> delete(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return client.build().delete().uri("/{id}", params).accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(Product.class).then();
    }
}
