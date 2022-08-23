package com.regulus.reactor.handler;

import com.regulus.reactor.dto.ProductRequest;
import com.regulus.reactor.dto.ProductResponse;
import com.regulus.reactor.service.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.*;

import java.net.URI;

@Component
public class ProductHandler {
    private final ProductService productService;

    public ProductHandler(ProductService productService) {
        this.productService = productService;
    }

    public Mono<ServerResponse> getAllProduct(ServerRequest request) {
        return ServerResponse.ok().body(productService.getAllProduct(), ProductResponse.class);
    }

    public Mono<ServerResponse> getProductById(ServerRequest request) {

        String id = request.pathVariable("id");
        return productService.getProductById(id).flatMap(productResponse ->
                ServerResponse.accepted().body(fromObject(productResponse)).switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> removeProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        return productService.getProductById(id).flatMap(productResponse ->
                        productService.removeProduct(productResponse.getId()).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveProduct(ServerRequest request) {
        Mono<ProductRequest> productRequestAddMono = request.bodyToMono(ProductRequest.class);
        return productService.saveProductV2(productRequestAddMono).flatMap(productResponse -> {
            return ServerResponse.created
                    (URI.create("/api/v2/product/".concat(productResponse.getId()))).body(fromObject(productResponse));
        });
    }
}
