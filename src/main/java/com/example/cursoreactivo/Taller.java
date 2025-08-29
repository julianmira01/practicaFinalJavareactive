package com.example.cursoreactivo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class Taller {
    public static Flux<String> flujoOrdenes(){
        Flux<Orden> ordenes = Flux.just(
                new Orden("juan", "123", 150),
                new Orden("ana", "456", 80),
                new Orden("luis", "789", 200),
                new Orden("maria", "101", 50),
                new Orden("pedro", "112", 120),
                new Orden("carlos", "113", 90),
                new Orden("manu", "111", 89.75)
        );

        return ordenes
                .map(orden -> new Orden(
                        orden.getUsuario().toUpperCase(),
                        "CC_" + orden.getCedula(),
                        (double) Math.round(orden.getMonto())
                ))
                .filter(orden -> orden.getMonto() >= 90)
                .flatMap(Taller::save);

    }
    private static Mono<String> save(Orden orden) {
        // Simula retardo de error al guardar en BD
        return Mono.delay(Duration.ofMillis(500))
                .flatMap(t -> {
                    if (Math.random() < 0.10) {
                        return Mono.error(new RuntimeException("onComplete: " + orden.getUsuario() + " - Error al guardar en BD"));
                    }
//                    System.out.println("Guardando orden: " + orden);
                    return Mono.just("La Orden ha sido guardada en BD: " + orden.getUsuario() + " - " + orden.getCedula());
                })
                .onErrorResume(error -> {
//                    System.err.println("onErrorResume: " + error.getMessage());
                    return Mono.just("Orden con error, se reintenta...");
                })
                .onErrorReturn("Orden fallida")
                .retry(2);
    }
}
