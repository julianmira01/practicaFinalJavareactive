package com.example.cursoreactivo.config;

import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.*;

@Data
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

    private String host;
    private int port;
    private String username;
    private String password;
}
