package com.example.cursoreactivo.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@RestController
public class Saludo {

    @GetMapping("/saludo")
    public Mono<String> saludo() {
        return Mono.just("¡Hola, bienvenido al curso de programación reactiva con Spring WebFlux!")
                .doOnNext(s -> System.out.println("Mensaje enviado: " + s))
                .doOnError(error -> System.err.println("Error al enviar el mensaje: " + error.getMessage()));
    }

    @GetMapping(value = "/saludo/nombre", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> sayName(@RequestParam String nombre) {
        return Mono.just("El nombre recibido es: " + nombre);
    }

    @GetMapping(value = "/saludo/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> sayNameVariable(@PathVariable String nombre) {
        return Mono.just("El nombre recibido es: " + nombre);
    }

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route(
                GET("/saludoF"),
                request -> ServerResponse.ok().bodyValue("Hola")
        );
    }
}
