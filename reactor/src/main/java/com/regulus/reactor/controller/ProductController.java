package com.regulus.reactor.controller;

import com.regulus.reactor.dto.ProductRequestAdd;
import com.regulus.reactor.dto.ProductRequestUpdate;
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
        return productService.findAll()
                .map(productResponses ->
                        new ResponseEntity<>(productResponses, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponse>> getProductById(@PathVariable String id) {
        return productService.findById(id).map(productResponse ->
            new ResponseEntity<>(productResponse, HttpStatus.OK))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<ProductResponse>> saveProduct( @RequestBody ProductRequestAdd request) {
        return productService.save(request)
                .map(productResponse -> ResponseEntity.created(URI.create("/api/product/".concat(productResponse.getId()))).body(productResponse))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<ProductResponse>> updateProduct(@RequestBody ProductRequestUpdate request, @PathVariable String id) {
        return productService.update(request, id)
                .map(productResponse -> ResponseEntity.created(URI.create("/api/product/".concat(productResponse.getId()))).body(productResponse))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/remove/{id}")
    public Mono<ResponseEntity<Void>> removeProduct(@PathVariable String id) {
        return productService.findById(id).flatMap(productResponse -> {
            return productService.delete(id).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));



    }
}
