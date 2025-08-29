package com.example.cursoreactivo.practicafinal.rest;

import com.example.cursoreactivo.cliente.ClienteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PracticaFinalRouter {

    @Bean
    public RouterFunction<ServerResponse> practicaFinalRouterr(PracticaFinalHandler practicaFinalHandler) {
        return RouterFunctions
                .route(PUT("/practicaFinal/modificarCliente/{numeroDocumento}"), practicaFinalHandler::updateClient)
                .andRoute(GET("/practicaFinal/buscarCliente/{numeroDocumento}"), practicaFinalHandler::getClientByDocumentNumber);
    }
}
