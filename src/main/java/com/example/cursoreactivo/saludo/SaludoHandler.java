package com.example.cursoreactivo.saludo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SaludoHandler {

    @Autowired
    private SaludoService saludoService;

    public Mono<ServerResponse> saludo(ServerRequest request) {
        String name = request.pathVariable("nombre");
        String saludo = saludoService.saludo(name);
        return ServerResponse.ok().bodyValue(saludo);
    }


}
