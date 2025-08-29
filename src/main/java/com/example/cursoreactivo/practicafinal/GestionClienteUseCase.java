package com.example.cursoreactivo.practicafinal;

import com.example.cursoreactivo.Cliente;
import com.example.cursoreactivo.cliente.ClientMessageProducer;
import com.example.cursoreactivo.cliente.ClienteService;
import com.example.cursoreactivo.cliente.EventoCliente;
import com.example.cursoreactivo.practicafinal.consumer.DelayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Component
public class GestionClienteUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionClienteUseCase.class);

    @Autowired
    private DelayService delayService;

    @Autowired
    private ClienteService clientService;

    @Autowired
    private MessageReplicaProducer messageReplicaProducer;

    public Mono<?> ejecutarEventoCliente(EventoCliente evento) {

        LOGGER.info("Inicio paralelizacion del flujo para el evento: {}", evento.getEvento());

        return Mono.zip(
                gestionarEventoCliente(evento),
                delayService.obtenerDelay(),
                agregarLog(evento),
                messageReplicaProducer.enviarEvento(evento.getEvento(), evento.getCliente()));
    }


    private Mono<?> agregarLog(EventoCliente evento) {
        return Mono.fromRunnable(() -> {
                    LOGGER.info("Log paralelizado para el cliente: {}", evento.getCliente().getNumeroDocumento());
                })
                .then(Mono.just(true));

    }

    private Mono<?> gestionarEventoCliente(EventoCliente evento) {
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
