package com.sistemasactivos.junit.model;

import com.sistemasactivos.junit.exception.DineroInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre("Andrés");

        String esperado = "Andrés";
        String real = cuenta.getNombre();

        assertNotNull(real);
        assertEquals(esperado, real);
        assertTrue(real.equals(esperado)); // Es lo mismo que hacer assertEquals(esperado, real);
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(new BigDecimal("1000.12345"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setNombre("Andrés");
        cuenta1.setSaldo(new BigDecimal("1000.12345"));

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setNombre("Andrés");
        cuenta2.setSaldo(new BigDecimal("1000.12345"));

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

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre("Andrés");
        cuenta.setSaldo(new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre("Andrés");
        cuenta.setSaldo(new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre("Andrés");
        cuenta.setSaldo(new BigDecimal("1000.12345"));

        /*
         * El metodo assertThrows devuelve la excepcion que se espera que se lance, en este caso
         * tiene que devolver una DineroInsuficienteException.
         */
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });

        String actual = exception.getMessage();
        String esperado = "Dinero insuficiente";

        // Comparo los mensajes del exception con el esperado
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setNombre("Andrés");
        cuenta1.setSaldo(new BigDecimal("1000.5"));

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setNombre("Julian");
        cuenta2.setSaldo(new BigDecimal("9500.25"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertEquals("9000.25", cuenta2.getSaldo().toPlainString());
        assertEquals("1500.5", cuenta1.getSaldo().toPlainString());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setNombre("Andrés");
        cuenta1.setSaldo(new BigDecimal("1000.5"));

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setNombre("Julian");
        cuenta2.setSaldo(new BigDecimal("9500.25"));

        Banco banco = new Banco();
        banco.agregarCuenta(cuenta1);
        banco.agregarCuenta(cuenta2);
        banco.setNombre("Banco del Estado");

        // tranfiero 500 de la cuenta2 a la cuenta1
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        /*
         * Con el metodo assertAll() se ejecutan todos los asserts que estan dentro de este metodo
         * de forma INDEPENDIENTE, es decir, si uno falla, los demas se ejecutan igual.
         */
        assertAll(
                () -> {
                    // verifico que de la cuenta2 se resten 500 pesos
                    assertEquals("9000.25", cuenta2.getSaldo().toPlainString());
                },
                () -> {
                    // verifico a la cuenta1 se sumen 500 pesos
                    assertEquals("1500.5", cuenta1.getSaldo().toPlainString());
                },
                () -> {
                    // verifico que el banco tenga 2 cuentas (cuenta1 y cuenta2)
                    assertEquals(2, banco.getCuentas().size());
                },
                () -> {
                    // verifico que el nombre del banco sea "Banco del Estado"
                    assertEquals("Banco del Estado", cuenta1.getBanco().getNombre());
                },
                () -> {
                    // verifico que el nombre de la cuenta1 sea "Andrés" con assertEquals
                    assertEquals("Andrés", banco.getCuentas().stream()
                            .filter(c -> c.getNombre().equals("Andrés"))
                            .findFirst()
                            .get()
                            .getNombre());
                },
                () -> {
                    // verifico que exista una cuenta con el nombre "Julian" con assertTrue
                    assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getNombre().equals("Julian")));
                }
        );
    }
}