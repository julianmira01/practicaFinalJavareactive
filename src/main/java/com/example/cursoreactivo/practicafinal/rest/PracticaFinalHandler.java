package com.example.cursoreactivo.practicafinal.rest;

import com.example.cursoreactivo.Cliente;
import com.example.cursoreactivo.cliente.ClientMessageProducer;
import com.example.cursoreactivo.cliente.ClienteService;
import com.example.cursoreactivo.practicafinal.AutoAckMessageConsumerEscenario1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class PracticaFinalHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticaFinalHandler.class);
    private final ClienteService clienteService;
    private final ClientMessageProducer clientMessageProducer;

    public PracticaFinalHandler(ClienteService clienteService, ClientMessageProducer clientMessageProducer) {
        this.clienteService = clienteService;
        this.clientMessageProducer = clientMessageProducer;
    }

    public Mono<ServerResponse> updateClient(ServerRequest request) {

        LOGGER.info("⚠️ Ingresando a actualizar cliente");
        String numeroDocumento = request.pathVariable("numeroDocumento");
        try {
            Long.parseLong(numeroDocumento);
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("numeroDocumento inválido: debe ser un número");
        }

        return request.bodyToMono(Cliente.class)
                .flatMap(client -> clienteService.updateClient(numeroDocumento, client)
                        .then(ServerResponse.ok().bodyValue(Map.of("mensaje", "Cliente actualizado", "Cliente", client)))
                )
                .onErrorResume(error -> {
                    Map<String, Object> body = Map.of("mensaje", "Error al actualizar cliente", "detalle", error.getMessage());
                    return ServerResponse.badRequest().bodyValue(body);
                });
    }

    public Mono<ServerResponse> getClientByDocumentNumber(ServerRequest request) {
        LOGGER.info("⚠️ Ingresando a obtener cliente por nro de documento");
        String numeroDocumento = request.pathVariable("numeroDocumento");
        try {
            Long.parseLong(numeroDocumento);
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("numeroDocumento inválido: debe ser un número");
        }

        return clienteService.findByNumeroDocumentoConRetardo(numeroDocumento)
                .flatMap(cliente -> ServerResponse.ok().bodyValue(cliente))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> {
                    Map<String, Object> body = Map.of("mensaje", "Error al buscar cliente", "detalle", error.getMessage());
                    return ServerResponse.badRequest().bodyValue(body);
                });
    }
}
