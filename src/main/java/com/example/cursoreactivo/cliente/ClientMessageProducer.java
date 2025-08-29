package com.example.cursoreactivo.cliente;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;
import com.example.cursoreactivo.Cliente;

@Component
public class ClientMessageProducer {

    @Autowired
    private Sender sender;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Void> enviarEvento(String tipo, Cliente cliente) {
        EventoCliente evento = new EventoCliente();
        evento.setEvento(tipo);
        evento.setCliente(cliente);

        try {
            byte[] body = objectMapper.writeValueAsBytes(evento);
            OutboundMessage mensaje = new OutboundMessage(
                    "cliente.exchange", "cliente.operacion", body
            );
            return sender.send(Mono.just(mensaje));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
