package com.sistemasactivos.junit.model;

import com.sistemasactivos.junit.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    /*
     * Defino una variable global para que sea accesible desde todos los metodos
     */
    private Cuenta cuenta;

    /*
     * Como todos los metodos usan la variable cuenta, puedo inicializarla en el metodo setUp()
     * y de esta forma, no tengo que inicializarla en cada metodo. Este metodo se ejecuta antes
     * de cada test.
     */
    @BeforeEach
    void setUp() {
        this.cuenta = new Cuenta();
        cuenta.setNombre("Andrés");
        cuenta.setSaldo(new BigDecimal("1000.12345"));
    }

    /*
     * Este metodo se ejecuta al finalizar cada test
     */
    @AfterEach
    void tearDown() {
        // de momento no hace nada
    }

    /*
     * Con la anotacion @DisplayName("nombre del test") se puede cambiar el nombre del test que aparece
     * en la consola de ejecucion de los tests. Sirve para darle un nombre mas descriptivo al test.
     *
     * Con la anotacion @Disabled se puede deshabilitar un test, de esta forma, cuando se ejecuten los
     * test, el que tenga esta anotacion va a ser salteado
     */
    @Disabled
    @Test
    @DisplayName("Probando nombre de la cuenta")
    void testNombreCuenta() {
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
    @DisplayName("Probando el saldo de la cuenta")
    void testSaldoCuenta() {
        assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), () -> "El saldo no es el esperado");
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0, () -> "El saldo no puede ser negativo");
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, () -> "El saldo debe ser mayor a cero");
    }

    @Test
    @DisplayName("Probando referencia de la cuenta")
    void testReferenciaCuenta() {
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
        assertEquals(cuenta, cuenta2, () -> "Las cuentas no son iguales");
    }

    @Test
    @DisplayName("Probando el metodo debito de la cuenta")
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
        assertEquals(900, cuenta.getSaldo().intValue(), () -> "El saldo no es el esperado");
        assertEquals("900.12345", cuenta.getSaldo().toPlainString(), () -> "El saldo no es el esperado");
    }

    @Test
    @DisplayName("Probando el metodo credito de la cuenta")
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
        assertEquals(1100, cuenta.getSaldo().intValue(), () -> "El saldo no es el esperado");
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString(), () -> "El saldo no es el esperado");
    }

    @Test
    @DisplayName("Probando el metodo debito de la cuenta con dinero insuficiente")
    void testDineroInsuficienteException() {

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
    @DisplayName("Probando el metodo transferir de la cuenta")
    void testTransferirDineroCuentas() {
        cuenta.setSaldo(new BigDecimal("1000.5"));

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setNombre("Julian");
        cuenta2.setSaldo(new BigDecimal("9500.25"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta, new BigDecimal(500));

        assertEquals("9000.25", cuenta2.getSaldo().toPlainString(), () -> "El saldo de la cuenta2 no es el esperado");
        assertEquals("1500.5", cuenta.getSaldo().toPlainString(), () -> "El saldo de la cuenta1 no es el esperado");
    }

    @Test
    @DisplayName("Probando relacion entre cuentas y banco")
    void testRelacionBancoCuentas() {
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setNombre("Julian");
        cuenta2.setSaldo(new BigDecimal("9500.25"));

        Banco banco = new Banco();
        banco.agregarCuenta(cuenta);
        banco.agregarCuenta(cuenta2);
        banco.setNombre("Banco del Estado");

        // tranfiero 500 de la cuenta2 a la cuenta1
        banco.transferir(cuenta2, cuenta, new BigDecimal(500));

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
                    assertEquals("1500.12345", cuenta.getSaldo().toPlainString(), () -> "El saldo de la cuenta1 no es el esperado");
                },
                () -> {
                    // verifico que el banco tenga 2 cuentas (cuenta1 y cuenta2)
                    assertEquals(2, banco.getCuentas().size(), () -> "El banco no tiene las cuentas esperadas");
                },
                () -> {
                    // verifico que el nombre del banco sea "Banco del Estado"
                    assertEquals("Banco del Estado", cuenta.getBanco().getNombre(), () -> "El nombre del banco no es el esperado");
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