package com.example.cursoreactivo.cliente;

import com.example.cursoreactivo.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class EventoCliente {
    private String evento;
    private Cliente cliente;
}
