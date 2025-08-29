package com.example.cursoreactivo.practicafinal;

import com.example.cursoreactivo.cliente.ClienteService;
import com.example.cursoreactivo.cliente.EventoCliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.ConsumeOptions;
import reactor.rabbitmq.Receiver;

import java.io.IOException;

@Component
public class AutoAckMessageConsumerEscenario1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoAckMessageConsumerEscenario1.class);

    @Autowired
    private Receiver receiver;

    @Autowired
    private ClienteService clientService;

    @Autowired
    private GestionClienteUseCase practicaFinalUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void iniciarConsumo() {
        receiver.consumeAutoAck("practica.final.auto.ack.queue", getQueueOptions())
                .flatMap(delivery -> {
                    try {
                        EventoCliente evento = objectMapper.readValue(delivery.getBody(), EventoCliente.class);
                        return practicaFinalUseCase.ejecutarEventoCliente(evento)
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

    private ConsumeOptions getQueueOptions() {
        return new ConsumeOptions()
                .qos(10);
    }
}
