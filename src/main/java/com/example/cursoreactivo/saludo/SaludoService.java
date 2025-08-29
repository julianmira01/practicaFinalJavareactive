package com.example.cursoreactivo.saludo;

import org.springframework.stereotype.Service;

@Service
public class SaludoService {

    public String saludo(String nombre){
        return "Hola " + nombre;
    }
}
