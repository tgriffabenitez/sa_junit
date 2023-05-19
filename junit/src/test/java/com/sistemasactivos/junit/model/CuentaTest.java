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

        /*
         * A los asserts se les puede pasar un parametro (tiene que ser el ultimo) que es un mensaje
         * que se muestra en caso de que el assert falle. Es util para saber que assert fallo y por que
         *
         * Las buenas practicas indican que este mensaje tiene que se pasado como una funcion lambda,
         * de esta forma, solo se crea el mensaje en caso de que el assert falle.
         */
        assertNotNull(real, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real, () -> "El nombre de la cuenta no es el esperado");
        assertTrue(real.equals(esperado)); // Es lo mismo que hacer assertEquals(esperado, real);
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(new BigDecimal("1000.12345"));

        assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), () -> "El saldo no es el esperado");
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0, () -> "El saldo no puede ser negativo");
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, () -> "El saldo debe ser mayor a cero");
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
        assertEquals(cuenta1, cuenta2, () -> "Las cuentas no son iguales");
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre("Andrés");
        cuenta.setSaldo(new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
        assertEquals(900, cuenta.getSaldo().intValue(), () -> "El saldo no es el esperado");
        assertEquals("900.12345", cuenta.getSaldo().toPlainString(), () -> "El saldo no es el esperado");
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre("Andrés");
        cuenta.setSaldo(new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
        assertEquals(1100, cuenta.getSaldo().intValue(), () -> "El saldo no es el esperado");
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString(), () -> "El saldo no es el esperado");
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
        }, () -> "El saldo es menor al monto a debitar");

        String actual = exception.getMessage();
        String esperado = "Dinero insuficiente";

        // Comparo los mensajes del exception con el esperado
        assertEquals(esperado, actual, () -> "El mensaje no es el esperado");
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

        assertEquals("9000.25", cuenta2.getSaldo().toPlainString(), () -> "El saldo de la cuenta2 no es el esperado");
        assertEquals("1500.5", cuenta1.getSaldo().toPlainString(), () -> "El saldo de la cuenta1 no es el esperado");
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
                    assertEquals("9000.25", cuenta2.getSaldo().toPlainString(), () -> "El saldo de la cuenta2 no es el esperado");
                },
                () -> {
                    // verifico a la cuenta1 se sumen 500 pesos
                    assertEquals("1500.5", cuenta1.getSaldo().toPlainString(), () -> "El saldo de la cuenta1 no es el esperado");
                },
                () -> {
                    // verifico que el banco tenga 2 cuentas (cuenta1 y cuenta2)
                    assertEquals(2, banco.getCuentas().size(), () -> "El banco no tiene las cuentas esperadas");
                },
                () -> {
                    // verifico que el nombre del banco sea "Banco del Estado"
                    assertEquals("Banco del Estado", cuenta1.getBanco().getNombre(), () -> "El nombre del banco no es el esperado");
                },
                () -> {
                    // verifico que el nombre de la cuenta1 sea "Andrés" con assertEquals
                    assertEquals("Andrés", banco.getCuentas().stream()
                            .filter(c -> c.getNombre().equals("Andrés"))
                            .findFirst()
                            .get()
                            .getNombre(), () -> "El nombre de la cuenta1 no es el esperado");
                },
                () -> {
                    // verifico que exista una cuenta con el nombre "Julian" con assertTrue
                    assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getNombre().equals("Julian")), () -> "No existe la cuenta de Julián en el banco");
                }
        );
    }
}