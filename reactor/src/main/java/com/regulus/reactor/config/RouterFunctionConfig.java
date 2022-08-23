package com.regulus.reactor.config;

import com.regulus.reactor.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ProductHandler productHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/api/v2/product"), request -> productHandler.getAllProduct(request))
                .andRoute(RequestPredicates.GET("/api/v2/product/{id}"), request -> productHandler.getProductById(request))
                .andRoute(RequestPredicates.POST("/api/v2/product/add"), request -> productHandler.saveProduct(request))
                .andRoute(RequestPredicates.DELETE("/api/v2/product/{id}"), request -> productHandler.removeProduct(request));
    }
}
