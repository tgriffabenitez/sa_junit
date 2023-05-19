package com.sistemasactivos.junit.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Andrés", new BigDecimal("1000.12345"));
        String esperado = "Andrés";
        String real = cuenta.getNombre();

        assertEquals(esperado, real);
        assertTrue(real.equals(esperado)); // Es lo mismo que hacer assertEquals(esperado, real);
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Andrés", new BigDecimal("1000.12345"));

        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

        /*
        * El metodo assertEquals(cuenta1, cuenta2) compara las direcciones de memoria de los
        * objetos intanciados.
        *
        * En este caso, como en la clase cuenta sobreescribi el metodo equals, ahora compara por
        * el valor de los atributos, es decir: verifica que los nombres
        * y el saldo sean iguales. Es por eso que este metodo ahora devuelve True
        */
        assertEquals(cuenta1, cuenta2);
    }
}