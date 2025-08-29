package com.example.cursoreactivo.cliente;

import com.example.cursoreactivo.Cliente;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ClienteRepository extends R2dbcRepository<Cliente, Long> {

    Mono<Cliente> findByNumeroDocumento(String numeroDocumento);
    Mono<Void> deleteByNumeroDocumento(String numeroDocumento);

    @Query("""
            SELECT *, pg_sleep(20) 
            FROM cliente c  
            where c.numero_documento = :nroDocumento
            """)
    Mono<Cliente> findByNumeroDocumentoConRetardo(String nroDocumento);

}
