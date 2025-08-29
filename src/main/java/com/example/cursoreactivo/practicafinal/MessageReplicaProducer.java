package com.example.cursoreactivo.practicafinal;

import com.example.cursoreactivo.Cliente;
import com.example.cursoreactivo.cliente.EventoCliente;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@Component
public class MessageReplicaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReplicaProducer.class);
    @Autowired
    private Sender sender;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Boolean> enviarEvento(String tipo, Cliente cliente) {
        EventoCliente evento = new EventoCliente();
        evento.setEvento(tipo);
        evento.setCliente(cliente);

        try {
            byte[] body = objectMapper.writeValueAsBytes(evento);
            OutboundMessage mensaje = new OutboundMessage(
                    "practica.final.replica.ex", "", body
            );
             return sender.send(Mono.just(mensaje))
                     .then(Mono.just(true));
        } catch (JsonProcessingException e) {
            LOGGER.info("Error cola replica: {}", evento.getEvento());
            return Mono.error(e);
        }
    }
}
