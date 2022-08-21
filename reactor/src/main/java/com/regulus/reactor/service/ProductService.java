package com.regulus.reactor.service;

import com.regulus.reactor.documents.Product;
import com.regulus.reactor.dto.ProductRequestAdd;
import com.regulus.reactor.dto.ProductRequestUpdate;
import com.regulus.reactor.dto.ProductResponse;
import com.regulus.reactor.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<List<ProductResponse>> findAll() {
        return productRepository.findAll()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .createAt(product.getCreateAt())
                        .build()).collectList();
    }

    public Mono<ProductResponse> findById(String id) {
        return productRepository.findById(id).map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .createAt(product.getCreateAt())
                .build());
    }

    public Mono<ProductResponse> save(ProductRequestAdd request) {
        return productRepository.save(Product.builder().name(request.getName()).price(request.getPrice())
                .createAt(new Date()).build()).map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .createAt(product.getCreateAt())
                .build());
    }

    public Mono<ProductResponse> saveV2(Mono<ProductRequestAdd> request) {
        return request.flatMap(produc -> productRepository.save(Product.builder().name(produc.getName())
                .price(produc.getPrice()).createAt(new Date()).build()
        )).map(product -> ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .createAt(product.getCreateAt())
                .build());
    }

    public Mono<ProductResponse> update(ProductRequestUpdate request, String id) {
        return productRepository.findByIdProduct(id).flatMap(product -> {
            return productRepository.save(Product.builder().id(id).name(request.getName()).price(request.getPrice()).build());
            }).map(pro -> ProductResponse.builder()
                .id(pro.getId())
                .name(pro.getName())
                .price(pro.getPrice())
                .createAt(pro.getCreateAt()).build());
    }

    public Mono<Void> delete( String id) {
        return productRepository.deleteById(id);
    }

    public Mono<Product> findByName(String name){
        return productRepository.findByNameProduct(name);
    }
}
