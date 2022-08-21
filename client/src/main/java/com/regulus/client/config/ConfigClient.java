package com.regulus.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ConfigClient {

    @Value("${config.base.endpoint}")
    private String url;

    @Bean
    @LoadBalanced
    public WebClient.Builder webClient(){
        return WebClient.builder().baseUrl(url);
    }


}
