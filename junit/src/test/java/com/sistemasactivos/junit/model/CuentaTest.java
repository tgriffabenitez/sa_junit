package com.sistemasactivos.junit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre("Andrés");
        String esperado = "Andrés";
        String real = cuenta.getNombre();

        Assertions.assertEquals(esperado, real);
    }
}