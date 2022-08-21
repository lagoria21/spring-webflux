package com.regulus.client.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler){
        return RouterFunctions.route(RequestPredicates.GET("/api/client"), productHandler::findAll)
                .andRoute(RequestPredicates.GET("/api/client/{id}"), productHandler::findById)
                .andRoute(RequestPredicates.POST("/api/client/add"), request -> productHandler.add(request))
                ;
    }
}
