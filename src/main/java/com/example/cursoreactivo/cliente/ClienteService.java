package com.example.cursoreactivo.cliente;

import com.example.cursoreactivo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public Mono<Cliente> createClient(Cliente cliente) {
        return verificarCliente(cliente)
                .flatMap(client -> clienteRepository.save(client));
    }

    public Mono<Cliente> getClient(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente no encontrado con el número de documento: " + numeroDocumento)))
                .onErrorResume(error -> Mono.error(new IllegalArgumentException("Error al buscar el cliente: " + error.getMessage())));
    }

    public Flux<Cliente> getAllClients() {
        return clienteRepository.findAll()
                .switchIfEmpty(Flux.error(new IllegalArgumentException("No se encontraron clientes.")))
                .onErrorResume(error -> Flux.error(new IllegalArgumentException("Error al obtener los clientes: " + error.getMessage())));
    }

    public Mono<Cliente> updateClient(String numeroDocumento, Cliente cliente) {
        return verificarCliente(cliente)
                .flatMap(client -> clienteRepository.findByNumeroDocumento(numeroDocumento))
                .flatMap(existingClient -> clienteRepository.save(this.buildCliente(cliente, existingClient.getId())))
                .switchIfEmpty(clienteRepository.save(cliente));
    }

    public Mono<Boolean> deleteClient(String numeroDocumentoABorrar) {
        return clienteRepository.findByNumeroDocumento(numeroDocumentoABorrar)
                .flatMap(clienteFound -> clienteRepository.delete(clienteFound)
                        .thenReturn(true)
                )
                .switchIfEmpty(Mono.just(false));
    }

    public Mono<Cliente> findByNumeroDocumentoConRetardo(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumentoConRetardo(numeroDocumento)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente no encontrado con el número de documento: " + numeroDocumento)))
                .onErrorResume(error -> Mono.error(new IllegalArgumentException("Error al buscar el cliente con retardo: " + error.getMessage())));
    }


    private Mono<Cliente> verificarCliente(Cliente cliente) {
        if (cliente.getTipoDocumento() == null || cliente.getTipoDocumento().isBlank()) {
            return Mono.error(new IllegalArgumentException("Tipo de documento inválido"));
        }
        if (cliente.getNumeroDocumento() == null || cliente.getNumeroDocumento().isBlank()) {
            return Mono.error(new IllegalArgumentException("Numero de documento inválido"));
        }
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            return Mono.error(new IllegalArgumentException("Nombre inválido"));
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            return Mono.error(new IllegalArgumentException("Email inválido"));
        }
        return Mono.just(cliente);
    }

    private Cliente buildCliente(Cliente cliente, Long id) {
        return Cliente.builder()
                .id(id)
                .tipoDocumento(cliente.getTipoDocumento())
                .numeroDocumento(cliente.getNumeroDocumento())
                .nombre(cliente.getNombre())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .build();
    }

    private Cliente buildCliente2(Cliente cliente, Cliente existingClient) {
        return existingClient.toBuilder()
                .tipoDocumento(cliente.getTipoDocumento())
                .numeroDocumento(cliente.getNumeroDocumento())
                .nombre(cliente.getNombre())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .build();
    }
}
