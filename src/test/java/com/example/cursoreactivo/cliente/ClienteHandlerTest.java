package com.example.cursoreactivo.cliente;

import com.example.cursoreactivo.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteHandlerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteHandler clienteHandler;


    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .tipoDocumento("CC")
                .numeroDocumento("123456789")
                .nombre("Julian Ramirez")
                .email("correo@correo.com")
                .build();
    }

    @Test
    void createClienteTest() {

        MockServerRequest request = MockServerRequest.builder().body(Mono.just(cliente));

        when(clienteService.createClient(cliente)).thenReturn(Mono.just(cliente));

        Mono<ServerResponse> response = clienteHandler.createClient(request);

        StepVerifier.create(response).expectNextMatches(responseExpect -> responseExpect.statusCode()
                .is2xxSuccessful()).verifyComplete();

    }

    @Test
    void getClientTest() {
        String numeroDocumento = "123456";
        when(clienteService.getClient(numeroDocumento)).thenReturn(Mono.just(cliente));

        MockServerRequest request = MockServerRequest.builder()
                .queryParam("numeroDocumento", numeroDocumento)
                .build();

        Mono<ServerResponse> response = clienteHandler.getClient(request);

        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();
    }
}
