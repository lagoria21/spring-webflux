package com.regulus.reactor.handler;

import com.regulus.reactor.documents.Category;
import com.regulus.reactor.documents.Product;
import com.regulus.reactor.dto.ProductResponse;
import com.regulus.reactor.service.impl.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductHandlerTest {
    @Autowired
    private WebTestClient webTestClient;

    //@MockBean
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Test
    public void findAllTestOk() {
        webTestClient.get().uri("/api/v2/product")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductResponse.class)
                .hasSize(10);
    }

    @Test
    public void findAllTestOkOne(){
        webTestClient.get().uri("/api/v2/product")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductResponse.class)
                .consumeWith(response -> {
                    List<ProductResponse> productResponses = response.getResponseBody();
                    productResponses.forEach(productResponse -> {
                        System.out.println(productResponse.getName());
                    });
                    Assertions.assertThat(productResponses.size() == 10).isTrue();
                });
    }

    @Test
    public void saveTest() {

        Product product = Product.builder().name("beto").price(20.0).category(Category.builder().name("electronico").build()).build();

        webTestClient.post().uri("/api/v2/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("beto");
    }

    @Test
    public void deleteTest() {

        Product product = productServiceImpl.findByName("Sony Camara HD Digital").block();

        webTestClient.delete()
                .uri("/api/v2/product/{id}", Collections.singletonMap("id", product.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();

    }

  /*  @Test
    public void findById(){

        BDDMockito.when(productService.findById("abc")).thenReturn(Mono.just(ProductResponse.builder().id("abc").name("beto").build()));

        webTestClient.get().uri("/api/v2/product/{id}", Collections.singletonMap("id", "abc"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ProductResponse.class)
                .consumeWith(response -> {
                   ProductResponse productResponse = response.getResponseBody();
                   Assertions.assertThat(productResponse.getId()).isEqualTo("beto");
                });
    }*/

    //   return RouterFunctions.route(RequestPredicates.GET("/api/v2/product"), request -> productHandler.findAll(request))
    //        .andRoute(RequestPredicates.GET("/api/v2/product/{id}"), request -> productHandler.findById(request))
    //        .andRoute(RequestPredicates.POST("/api/v2/product/add"), request -> productHandler.add(request))
    //        .andRoute(RequestPredicates.DELETE("/api/v2/product/{id}"), request -> productHandler.delete(request));
}
