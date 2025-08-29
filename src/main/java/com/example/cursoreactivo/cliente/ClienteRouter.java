package com.example.cursoreactivo.cliente;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ClienteRouter {

    @Bean
    public RouterFunction<ServerResponse> clienteRouterr(ClienteHandler clienteHandler) {
        return RouterFunctions
                .route(POST("/clienteFuncional"),clienteHandler::createClient)
                .andRoute(GET("/clienteFuncional/{numeroDocumento}"), clienteHandler::getClient)
                .andRoute(PUT("/clienteFuncional/{numeroDocumento}"), clienteHandler::updateClient)
                .andRoute(DELETE("/clienteFuncional/{numeroDocumento}"), clienteHandler::deleteClient)
                .andRoute(GET("/clienteFuncional"), clienteHandler::getAllClients);
    }
}
