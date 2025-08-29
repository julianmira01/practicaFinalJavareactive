package com.example.cursoreactivo;

import java.util.Objects;

public class Orden {
    private String usuario;
    private String cedula;
    private double monto;

    public Orden(String usuario, String cedula, double monto) {
        this.usuario = usuario;
        this.cedula = cedula;
        this.monto = monto;
    }

    public String getUsuario() { return usuario; }
    public String getCedula() { return cedula; }
    public double getMonto() { return monto; }


    @Override
    public String toString() {
        return "Orden{" +
                "usuario='" + usuario + '\'' +
                ", cedula='" + cedula + '\'' +
                ", monto=" + monto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Orden orden)) return false;
        return Double.compare(monto, orden.monto) == 0 && Objects.equals(usuario, orden.usuario) && Objects.equals(cedula, orden.cedula);
    }
}
