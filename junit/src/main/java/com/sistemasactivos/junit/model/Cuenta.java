package com.sistemasactivos.junit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {
    private String nombre;
    private BigDecimal saldo;

    /*
    * sobreescribo el metodo equals para que compare por atributos
    * y no por la direccion de memoria del objeto instanciado
    */
    @Override
    public boolean equals(Object obj) {
        Cuenta cuenta = (Cuenta) obj; // casteo el objeto a tipo Cuenta
        if (!(obj instanceof Cuenta))
            return false;

        if (this.nombre == null || this.saldo == null)
            return false;

        boolean nombreIguales = this.nombre.equals(cuenta.getNombre());
        boolean saldoIguales = this.saldo.equals(cuenta.getSaldo());

        return nombreIguales && saldoIguales;
    }
}
