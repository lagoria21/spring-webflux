package com.regulus.reactor.handler;

import com.regulus.reactor.dto.ProductRequestAdd;
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

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().body(productService.findAll(), ProductResponse.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");

        //Mono<ProductResponse> responseMono = productService.findById(id);

        return productService.findById(id).flatMap(productResponse ->
                ServerResponse.accepted().body(fromObject(productResponse)));

       // return productService.findById(id).map(productResponse ->
        //                ServerResponse.ok().body(productResponse, ProductResponse.class)).then(ServerResponse.notFound().build());
               // .switchIfEmpty(ServerResponse.notFound().build());
    }

        /*	public Mono<ServerResponse> findUser(ServerRequest request) {
		String userId = request.pathVariable("userId");
		Mono<UserVo> matchedUser = userMapper.modelToView(userService.find(Long.parseLong(userId)));
		return matchedUser.flatMap(u -> ServerResponse.status(HttpStatus.FOUND).contentType(MediaType.APPLICATION_JSON)
				.body(matchedUser, UserVo.class)).switchIfEmpty(notFound);
*/

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return productService.findById(id).flatMap(productResponse -> productService.delete(productResponse.getId()).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> add(ServerRequest request){
        Mono<ProductRequestAdd> productRequestAddMono = request.bodyToMono(ProductRequestAdd.class);
        return productService.saveV2(productRequestAddMono).flatMap(productResponse -> {
            return ServerResponse.created(URI.create("/api/v2/product/".concat(productResponse.getId()))).body(fromObject(productResponse));
        });
    }
}
