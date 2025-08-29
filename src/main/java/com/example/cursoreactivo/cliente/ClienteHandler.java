package com.example.cursoreactivo.cliente;

import com.example.cursoreactivo.Cliente;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ClienteHandler {

    private final ClienteService clienteService;
    private final ClientMessageProducer clientMessageProducer;

    public ClienteHandler(ClienteService clienteService, ClientMessageProducer clientMessageProducer) {
        this.clienteService = clienteService;
        this.clientMessageProducer = clientMessageProducer;
    }

    public Mono<ServerResponse> createClient(ServerRequest request) {
        return request.bodyToMono(Cliente.class)
                .flatMap(cliente -> clientMessageProducer.enviarEvento("crear", cliente)
                        .then(ServerResponse.accepted().bodyValue(Map.of("mensaje", "Cliente enviado", "Cliente", cliente)))
                )
                .onErrorResume(error -> {
                    Map<String, Object> body = Map.of("mensaje", "Error al crear cliente", "detalle", error.getMessage());
                    return ServerResponse.badRequest().bodyValue(body);
                });
    }

    public Mono<ServerResponse> updateClient(ServerRequest request) {

        try {
            Long.parseLong(request.pathVariable("numeroDocumento"));
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("numeroDocumento inválido: debe ser un número");
        }

        return request.bodyToMono(Cliente.class)
                .flatMap(client -> clientMessageProducer.enviarEvento("editar", client)
                        .then(ServerResponse.accepted().bodyValue(Map.of("mensaje", "Cliente enviado para actualizar", "Cliente", client)))
                )
                .onErrorResume(error -> {
                    Map<String, Object> body = Map.of("mensaje", "Error al actualizar cliente", "detalle", error.getMessage());
                    return ServerResponse.badRequest().bodyValue(body);
                });
//                .flatMap(cliente -> clienteService.updateClient(request.pathVariable("numeroDocumento"), cliente))
//                .flatMap(cliente -> ServerResponse.ok().bodyValue(cliente))
//                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteClient(ServerRequest request) {
        try {
            Long.parseLong(request.pathVariable("numeroDocumento"));
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("numeroDocumento inválido: debe ser un número");
        }

        return clienteService.deleteClient(request.pathVariable("numeroDocumento"))
                .flatMap(eliminado -> eliminado ? ServerResponse.noContent().build() :
                        ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getClient(ServerRequest request) {

        try {
            Long.parseLong(request.pathVariable("numeroDocumento"));
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("numeroDocumento inválido: debe ser un número");
        }
        return clienteService.getClient(request.pathVariable("numeroDocumento"))
                .flatMap(cliente -> ServerResponse.ok().bodyValue(cliente))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAllClients(ServerRequest request) {
        return clienteService.getAllClients()
                .collectList()
                .flatMap(clientes -> {
                    if (clientes.isEmpty()) {
                        return ServerResponse.notFound().build();
                    } else {
                        return ServerResponse.ok().bodyValue(clientes);
                    }
                })
                .onErrorResume(error ->
                        ServerResponse.badRequest().bodyValue("Error al obtener los clientes: " + error.getMessage())
                );
    }


}
