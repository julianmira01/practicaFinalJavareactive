package com.example.cursoreactivo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
public class CursoreactivoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CursoreactivoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		Taller.flujoOrdenes()
//				.subscribe(
//						item -> System.out.println("Recibido: " + item),
//						error -> System.err.println("Error: " + error.getMessage()),
//						() -> System.out.println("Flujo completado sin errores")
//				);
//		Mono<String> var1 = Mono.just("Hola");
//		var1.subscribe(System.out::println);

//		Flux<Integer> numeros = Flux.range(1, 10);
//				.map(i -> i * 2)
//				.filter(i -> i % 3 == 0);

//		numeros.subscribe(System.out::println);


//		Flux<Integer> numeros = Flux.range(1, 10)
//				.map(i -> i * 2)
//				.filter(i -> i % 3 == 0);
//		numeros.subscribe(System.out::println);


//		List<Integer> listaUser = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//
//		Flux<Integer> flujoLista = Flux.fromIterable(listaUser)
//				.map(item -> {
//					if(item <0){
//						throw new IllegalArgumentException("El número no puede ser negativo: " + item);
//					}
//					return item;
//				});
//
//		flujoLista
//				.subscribe(item -> System.out.println(item),
//						error -> System.err.println("Error: " + error.getMessage()),
//						() -> System.out.println("Flujo completado sin errores"));




//		Flux<String> letras = (Flux<String>) Flux.just("a", "b", "c", "d", "e")
//				.map(String::toLowerCase)
//				.filter(letra -> !letra.equals("B"))
//				.subscribe(System.out::println);

//		String mensaje = Integer.parseInt("1");


//		Flux.just("Sebastian R", "Arbey", "Eli", "Carlo", "Sebastiian G", "Duvan", "Ingrid", "Ana")
//				.map(String::toUpperCase)
//				.map(String::trim)
//				.filter(mensaje -> mensaje.length() > 5)
//				.subscribe(System.out::println);


//		Flux<String> mensajes2 = (Flux<String>) Flux.just("Sebastian R", "Arbey", "Eli", "Carlo", "Sebastiian G", "Duvan", "Ingrid", "Ana");
//
//		mensajes2
//				.map(String::toUpperCase)
//				.map(String::trim)
//				.filter(mensaje -> mensaje.length() > 5)
//				.subscribe(System.out::println);


//		Flux.range(1,10)
//				.take(3)
//				.subscribe(System.out::println);


//		Flux.range(1,10)
//				.skip(5)
//				.subscribe(System.out::println);


//		Flux.range(1,10)
//				.limitRate(5)
//				.subscribe(System.out::println);


//		Flux.just(1,2,3)
//				.concatWith(Flux.error( new RuntimeException("Error simulado")))
//				.onErrorResume(error ->{
//					System.err.println("Ocurrió un error: " + error.getMessage());
//					return Flux.just(100,200);
//				})
//				.subscribe(System.out::println);
//
//
//
//		Flux.just(1,2,3)
//				.concatWith(Flux.error( new RuntimeException("Error simulado")))
//				.onErrorReturn(900)
//				.subscribe(System.out::println);


//		Flux.<String>create(emitter -> {
//			System.out.println("Empezando a emitir mensajes...");
//			emitter.next("Mensaje 2");
//			emitter.error(new RuntimeException());
//			// emitter.error(new RuntimeException("Error simulado"));
//		}).retry(2)
//				.subscribe()
	}
}
