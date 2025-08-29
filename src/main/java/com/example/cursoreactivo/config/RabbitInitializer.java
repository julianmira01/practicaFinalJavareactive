package com.example.cursoreactivo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.BindingSpecification;
import reactor.rabbitmq.ExchangeSpecification;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Sender;

@Component
public class RabbitInitializer {

    @Autowired
    private Sender sender;

    @PostConstruct
    public void configurarRabbit() {
        sender.declareExchange(
                        ExchangeSpecification.exchange("cliente.exchange").type("direct"))
                .then(sender.declareQueue(QueueSpecification.queue("cliente.eventos.queue")))
                .then(sender.bind(
                        BindingSpecification.binding()
                                .exchange("cliente.exchange")
                                .queue("cliente.eventos.queue")
                                .routingKey("cliente.operacion")
                ))
                .doOnSuccess(v -> System.out.println("✔️ Exchange, queue y binding creados"))
                .doOnError(e -> System.err.println("❌ Error creando la infraestructura: " + e.getMessage()))
                .subscribe();
    }
}
