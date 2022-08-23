package com.regulus.client.controller;

import com.regulus.client.dto.Product;
import com.regulus.client.dto.ProductRequestAdd;
import com.regulus.client.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;


@Component
public class ProductHandler {

    @Autowired
    private ProductService productService;

    public Mono<ServerResponse> getAllProduct() {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(), Product.class);
    }


    public Mono<ServerResponse> getProductById(ServerRequest request) {
        String id = request.pathVariable("id");
        return productService.findById(id).flatMap(productResponse ->
                ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(productResponse)))
                       // .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> {
                    WebClientResponseException exception = (WebClientResponseException) error;
                    if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                        Map<String, Object> body = new HashMap<>();
                        body.put("error", "No existe producto: ".concat(exception.getMessage()));
                        body.put("status", exception.getStatusCode());
                        return ServerResponse.status(HttpStatus.NOT_FOUND).syncBody(body);
                    }
                    return Mono.error(exception);
                })

                ;
    }


    public Mono<ServerResponse> removeProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        return productService.findById(id).flatMap(productResponse -> productService.delete(productResponse.getId()).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> {
                    WebClientResponseException responseException = (WebClientResponseException) error;
                    if(responseException.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return ServerResponse.notFound().build();
                    }
                    return Mono.error(responseException);
                })
                ;
    }

    public Mono<ServerResponse> saveProduct(ServerRequest request){
        Mono<ProductRequestAdd> productRequestAddMono = request.bodyToMono(ProductRequestAdd.class);
        return productRequestAddMono.flatMap(product -> productService.save(product))
                .flatMap(p -> ServerResponse.created(URI.create("/api/v2/product/".concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(p))
                .onErrorResume(error -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) error;
                    if(errorResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
                        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(errorResponse.getResponseBodyAsString());
                    }
                    return Mono.error(errorResponse);
                });

    }
}
