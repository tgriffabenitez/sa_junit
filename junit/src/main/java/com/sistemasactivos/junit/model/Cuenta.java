package com.sistemasactivos.junit.model;

import com.sistemasactivos.junit.exception.DineroInsuficienteException;
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


    public void debito(BigDecimal monto) {
        /*
        * Como BigDecial es inmutable, no puedo hacer this.sado.subtract(monto)
        * ya que el metodo subtract devuelve un nuevo objeto BigDecimal
        * y no modifica el valor de this.saldo
        *
        * Por eso, tengo que asignar el valor devuelto por el metodo subtract
        */
        BigDecimal nuevoSaldo = this.saldo.subtract(monto);

        // verifico que el saldo no sea negativo, si lo es, lanzo una excepcion
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0)
            throw new DineroInsuficienteException("Dinero insuficiente");

        this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto) {
        /*
         * Como BigDecial es inmutable, no puedo hacer this.sado.add(monto)
         * ya que el metodo subtract devuelve un nuevo objeto BigDecimal
         * y no modifica el valor de this.saldo
         *
         * Por eso, tengo que asignar el valor devuelto por el metodo add
         */
        this.saldo = this.saldo.add(monto);
    }




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
