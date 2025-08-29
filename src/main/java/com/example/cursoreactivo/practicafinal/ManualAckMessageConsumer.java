package com.example.cursoreactivo.practicafinal;

import com.example.cursoreactivo.cliente.EventoCliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.AcknowledgableDelivery;
import reactor.rabbitmq.ConsumeOptions;
import reactor.rabbitmq.Receiver;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Function;

@Component
public class ManualAckMessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManualAckMessageConsumer.class);

    @Autowired
    private GestionClienteUseCase practicaFinalUseCase;

    @Autowired
    private Receiver receiver;


    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostConstruct
    public void iniciarConsumo() {
        receiver.consumeManualAck("practica.final.manual.ack.queue", getQueueOptions())
                .flatMap(delivery -> {
                    try {
                        EventoCliente evento = objectMapper.readValue(delivery.getBody(), EventoCliente.class);
                        return practicaFinalUseCase.ejecutarEventoCliente(evento)
                                .doOnSuccess(result -> LOGGER.info("✅ Evento procesado: {}", evento.getEvento()))
                                .switchIfEmpty(Mono.defer(() -> {
                                    LOGGER.warn("⚠️ Cliente no encontrado: {}", evento.getEvento());
                                    return Mono.empty();
                                }))
                                .then(Mono.fromRunnable(() -> {
                                    LOGGER.info("✅ ACK enviado para el mensaje: {}", evento.getEvento());
                                    delivery.ack();
                                }))
                                .onErrorResume(error -> {
                                    LOGGER.error("❌ Error procesando evento: {} - {}", evento.getEvento(), error.getMessage());
                                    delivery.nack(true);
                                    return Mono.empty();
                                });
                    } catch (IOException e) {
                        LOGGER.error("❌ Error deserializando mensaje", e);
                        return Mono.empty();
                    }
                }, 10)
                .subscribe();
    }

    private ConsumeOptions getQueueOptions() {
        return new ConsumeOptions()
                .qos(10);
    }

//    private static Mono<Boolean> acceptMessage(AcknowledgableDelivery delivery) {
//        return Mono.defer(() -> {
//            delivery.ack();
//            return Mono.just(true);
//        });
//    }
//
//    private static Function<Mono<Void>, Mono<Boolean>> ackMessage(AcknowledgableDelivery delivery) {
//        return process -> process
//                .then(acceptMessage(delivery))
//                .timeout(Duration.ofMinutes(100000));
//    }

}
