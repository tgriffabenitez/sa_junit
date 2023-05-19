package com.sistemasactivos.junit.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Andrés", new BigDecimal("1000.12345"));
        String esperado = "Andrés";
        String real = cuenta.getNombre();

        assertEquals(esperado, real);
        assertTrue(real.equals(esperado)); // Es lo mismo que hacer assertEquals(esperado, real);
    }

}