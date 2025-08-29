package com.example.cursoreactivo.cliente;

import com.example.cursoreactivo.Cliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Receiver;

import java.io.IOException;

//@Component
public class ClientMessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageConsumer.class);

    @Autowired
    private Receiver receiver;

    @Autowired
    private ClienteService clientService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void iniciarConsumo() {
        receiver.consumeAutoAck("cliente.eventos.queue")
                .flatMap(delivery -> {
                    try {
                        EventoCliente evento = objectMapper.readValue(delivery.getBody(), EventoCliente.class);
                        return despacharEvento(evento)
                                .doOnSuccess(result -> LOGGER.info("✅ Evento procesado: {}", evento.getEvento()))
                                .switchIfEmpty(Mono.defer(() -> {
                                    LOGGER.warn("⚠️ Cliente no encontrado: {}", evento.getEvento());
                                    return Mono.empty();
                                }))
                                .onErrorResume(error -> {
                                    LOGGER.error("❌ Error procesando evento: {} - {}", evento.getEvento(), error.getMessage());
                                    return Mono.empty();
                                });
                    } catch (IOException e) {
                        LOGGER.error("❌ Error deserializando mensaje", e);
                        return Mono.empty();
                    }
                })
                .subscribe();
    }

    private Mono<?> despacharEvento(EventoCliente evento) {
        String tipo = evento.getEvento();
        Cliente cliente = evento.getCliente();

        return switch (tipo.toLowerCase()) {
            case "crear" -> clientService.createClient(cliente);
            case "editar" -> clientService.updateClient(cliente.getNumeroDocumento(), cliente);
            case "eliminar" -> clientService.deleteClient(cliente.getNumeroDocumento());
            default -> Mono.error(new IllegalArgumentException("Evento no soportado: " + tipo));
        };
    }
}
