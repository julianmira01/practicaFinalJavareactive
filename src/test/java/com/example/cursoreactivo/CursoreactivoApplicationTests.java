package com.example.cursoreactivo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class CursoreactivoApplicationTests {

//	@Test
//	void contextLoads() {
//		StepVerifier.create(Mono.just("Hello, World!"))
//				.expectNext("Hello, World!")
//				.verifyComplete();
//
//		Flux<String> letras = Flux.just("A", "B", "C");
//
//		//Falla porque no son flujos iguales
//		StepVerifier.create(letras)
//				.expectNext("A", "H", "C")
//				.verifyComplete();
//
//		//Verifica que el flujo sea igual
//		StepVerifier.create(letras)
//				.assertNext(n -> n.equals("A"))
//				.assertNext(n -> n.equals("B"))
//				.assertNext(n -> n.equals("C"))
//				.verifyComplete();
//
//
//	}

	@Test
	void testError(){
		Mono<String> errorMono = Mono.error(new RuntimeException("Error occurred"));
		StepVerifier.create(errorMono)
				.expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
						"Error occurred".equals(throwable.getMessage()))
				.verify();
	}

}
