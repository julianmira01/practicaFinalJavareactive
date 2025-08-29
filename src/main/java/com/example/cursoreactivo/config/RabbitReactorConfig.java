package com.example.cursoreactivo.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.rabbitmq.*;

@Configuration
public class RabbitReactorConfig {

    @Autowired
    private RabbitMQProperties properties;

    @Bean
    public Sender sender() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUsername(properties.getUsername());
        factory.setPassword(properties.getPassword());

        SenderOptions options = new SenderOptions().connectionFactory(factory);
        return RabbitFlux.createSender(options);
    }

    @Bean
    public Receiver receiver() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUsername(properties.getUsername());
        factory.setPassword(properties.getPassword());

        ReceiverOptions options = new ReceiverOptions().connectionFactory(factory);
        return RabbitFlux.createReceiver(options);
    }
}
