package com.example.cursoreactivo.practicafinal.consumer;

import com.example.cursoreactivo.dto.DelayDto;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DelayService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DelayService.class);
    private final WebClient webClient;

    public DelayService() {
        this.webClient = WebClient.builder().baseUrl("https://postman-echo.com/delay/10").build();
    }

    public Mono<DelayDto> obtenerDelay() {
        return webClient.get()
                .retrieve()
                .bodyToMono(DelayDto.class)
                .map( delayDto -> {
                    LOGGER.info("Delay obtenido: {}", delayDto);
                    return delayDto;
                });
    }
}
