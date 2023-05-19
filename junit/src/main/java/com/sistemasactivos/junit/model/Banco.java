package com.sistemasactivos.junit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Banco {
    private String nombre;
    private List<Cuenta> cuentas = new ArrayList<>();

    public Banco(String nombre) {
        this.nombre = nombre;
    }

    public Banco(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
        cuenta.setBanco(this);
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {
        origen.debito(monto);
        destino.credito(monto);
    }

}
