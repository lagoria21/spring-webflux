package com.regulus.reactor;

import com.regulus.reactor.documents.Category;
import com.regulus.reactor.documents.Product;
import com.regulus.reactor.repository.ProductRepository;
import com.regulus.reactor.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
@Slf4j
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@Autowired
	ReactiveMongoTemplate reactiveMongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		reactiveMongoTemplate.dropCollection("product").subscribe();
		reactiveMongoTemplate.dropCollection("category").subscribe();

		Category electronico = Category.builder().name("Electrónico").build();
		Category deporte = Category.builder().name("Deporte").build();
		Category computacion = Category.builder().name("Computación").build();
		Category muebles = Category.builder().name("Muebles").build();

		Flux.just(electronico, deporte, computacion, muebles)
				.flatMap(category -> categoryServiceImpl.saveCategory(category))
				.doOnNext(c ->{
					log.info("Categoria creada: " + c.getName() + ", Id: " + c.getId());
				}).thenMany(
						Flux.just(
								Product.builder().name("TV Panasonic Pantalla LCD").price(456.89).category(electronico).build(),
								Product.builder().name("Sony Camara HD Digital").price(177.89).category(electronico).build(),
								Product.builder().name("Apple iPod").price(46.89).category(electronico).build(),
								Product.builder().name("Sony Notebook").price(846.89).category(electronico).build(),
								Product.builder().name("Hewlett Packard Multifuncional").price(200.89).category(computacion).build(),
								Product.builder().name("Bianchi Bicicleta").price(70.89).category(deporte).build(),
								Product.builder().name("HP Notebook Omen 17").price(2500.89).category(computacion).build(),
								Product.builder().name("Mica Cómoda 5 Cajones").price(150.89).category(electronico).build(),
								Product.builder().name("TV Sony Bravia OLED 4K Ultra HD").price(2255.89).category(electronico).build()
								)
								.flatMap(producto -> {
									producto.setCreateAt(new Date());
									return productRepository.save(producto);
								})
				)
				.subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getName()));
		}
}
