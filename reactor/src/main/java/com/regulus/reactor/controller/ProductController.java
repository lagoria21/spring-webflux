package com.regulus.reactor.controller;

import com.regulus.reactor.dto.ProductRequest;
import com.regulus.reactor.dto.ProductResponse;
import com.regulus.reactor.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public Mono<ResponseEntity<List<ProductResponse>>> getAllProducts() {
        return productService.getAllProduct()
                .map(productResponses ->
                        new ResponseEntity<>(productResponses, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponse>> getProductById(@PathVariable String id) {
        return productService.getProductById(id).map(productResponse ->
                        new ResponseEntity<>(productResponse, HttpStatus.OK))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<ProductResponse>> saveProduct(@RequestBody ProductRequest request) {
        return productService.saveProduct(request)
                .map(productResponse -> ResponseEntity.created(URI.create("/api/product/".concat(productResponse.getId()))).body(productResponse))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<ProductResponse>> updateProduct(@RequestBody ProductRequest request, @PathVariable String id) {
        return productService.updateProduct(request, id)
                .map(productResponse -> ResponseEntity.created(URI.create("/api/product/".concat(productResponse.getId()))).body(productResponse))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/remove/{id}")
    public Mono<ResponseEntity<Void>> removeProduct(@PathVariable String id) {
        return productService.getProductById(id).flatMap(productResponse ->
             productService.removeProduct(id).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
        ).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }
}
