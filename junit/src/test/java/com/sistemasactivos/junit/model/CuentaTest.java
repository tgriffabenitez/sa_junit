package com.sistemasactivos.junit.model;

import com.sistemasactivos.junit.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

class CuentaTest {

    /* Defino una variable global para que sea accesible desde todos los metodos */
    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        /*
         * Como todos los metodos usan la variable cuenta, puedo inicializarla en el metodo setUp()
         * y de esta forma, no tengo que inicializarla en cada metodo. Este metodo se ejecuta antes
         * de cada test.
         */

        this.cuenta = new Cuenta();
        cuenta.setNombre("Andrés");
        cuenta.setSaldo(new BigDecimal("1000.12345"));
    }

    @AfterEach
    void tearDown() {
        // Este metodo se ejecuta al finalizar cada test
    }

    @BeforeAll
    static void beforeAll() {
        // Este metodo se ejecuta al inicio de todos los tes
        System.out.println("Se ejecuta antes de todos los test");
    }

    @AfterAll
    static void afterAll() {
        // Este metodo se ejecuta al finalizar todos los test
        System.out.println("Se ejecuta despues de todos los test");
    }

    @Nested
    @Tag("tag1")
    @DisplayName("Probando metodos relacionados con el nombre y el saldo")
    class CuentaTestNombreSaldo {
        /* Dentro de cada clase anidada, puedo definir metodos @BeforeEach y @AfterEach */

        /*
         * Con la anotacion @Tag sirve para ordenar las clases o metodos a la hora de ejecutarlas.
         * Por ejemplo, puedo ejecutar todas la clases y/o metodos que tengan el tag("tag1"). Las
         * clases y metodos pueden tener varios tags.
         *
         * Para ejecutar los test con tags, tengo que ir a la configuracion del intelli y elegir
         * la opcion "Build and run" y elegir Tags en vez de Class
         */

        @Disabled
        @Test
        @DisplayName("Probando nombre de la cuenta")
        void testNombreCuenta() {
            /*
             * Con la anotacion @DisplayName("nombre del test") se puede cambiar el nombre del test que aparece
             * en la consola de ejecucion de los tests. Sirve para darle un nombre mas descriptivo al test.
             *
             * Con la anotacion @Disabled se puede deshabilitar un test, de esta forma, cuando se ejecuten los
             * test, el que tenga esta anotacion va a ser salteado
             */

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
        @Tag("tag1")
        @Tag("tag2")
        @DisplayName("Probando el saldo de la cuenta")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), () -> "El saldo no es el esperado");
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0, () -> "El saldo no puede ser negativo");
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, () -> "El saldo debe ser mayor a cero");
        }

        @Test
        @Tag("tag3")
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
        @DisplayName("Probando el saldo de la cuenta en ambiente dev 1")
        void testSaldoCuentaDev1() {
            /*
             * Este test solo se ejecuta si la variable de ambiente ENV es igual a DEV. En caso contrario
             * el test de deshabilita y no se ejecuta.
             *
             * El metodo assumeTrue() si es true, continua con la ejecucion del test, en caso contrario
             * deshabilita los asserts que esten debajo de este metodo.
             *
             * Esto es util por ejemplo si queremos realizar un test y tenemos una varibale o alguna
             * condicion que no dependa de nosotros, por ejemplo, el estado de un servidor.
             */

            // me fijo si la variable de ambiente ENV es igual a DEV
            boolean esDev = "dev".equals(System.getProperty("ENV"));

            assumeTrue(esDev); // si esto falla no ejecuto lo de abajo
            assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), () -> "El saldo no es el esperado");
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0, () -> "El saldo no puede ser negativo");
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, () -> "El saldo debe ser mayor a cero");
        }

        @Test
        @DisplayName("Probando el saldo de la cuenta en ambiente dev 2")
        void testSaldoCuentaDev2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));

            /*
             * Otra variante es usar assumingThat(condicion, ejecucion)
             * Si la condicion se cumple, ejecuta lo que esta dentro del lambda, en caso contrario
             * ejecuta los assert que esta debajo de este metodo.
             */
            assumingThat(esDev, () -> {
                assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
                assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), () -> "El saldo no es el esperado");
                assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0, () -> "El saldo no puede ser negativo");
                assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, () -> "El saldo debe ser mayor a");
            });

            // Si la condicion de arriba no se cumple ejecuta el resto de metodos
            System.out.println("Dev " + esDev + " se ejecuto igual");
        }
    }

    @Nested
    @DisplayName("Probando metodos de la clase Cuenta")
    class CuentaOperacionesTest {
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

    @Nested
    @DisplayName("Probando metodos relacionados con el sistema operativo")
    class SistemaOperativoTest {
        /*
         * Esta es una clase anidada (nested) que sirve para organizar los tests segun
         * una categoria, en este caso organizo los tests segun el sistema operativo
         */

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
            // Este test solo se ejecuta en Windows
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
            // Este test solo se ejecuta en Linux y Mac
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
            // Este test no se ejecuta en Windows
        }
    }

    @Nested
    @DisplayName("Probando metodos relacionados con la version de Java")
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8() {
            // Este test solo se ejecuta en Java 8
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void soloJdk17() {
            // Este test solo se ejecuta en Java 17
        }

        @Test
        @DisabledOnJre(JRE.JAVA_15)
        void noJdk15() {
            // Este test no se ejecuta en Java 15
        }
    }

    @Nested
    @DisplayName("Probando metodos relacionados con las propiedades del sistema")
    class SistemPropertiesTest {
        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "17.*")
        void testJavaVersion() {
            // Este test solo se ejecuta si la version de Java es 17
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
        void testJavaVersion64() {
            // Este test solo se ejecuta si la version de Java es 64 bits
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "tmg")
        void testUserName() {
            // Este test solo se ejecuta si el nombre de usuario es tmg
        }
    }

    @Nested
    @DisplayName("Probando metodos relacionados con variables de ambiente")
    class VariablesDeAmbiente {
        @Test
        void imprimirVariablesAmbienteTest() {
            // De esta forma puedo obtener todas las variables de ambiente del sistema
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + " = " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-17.*")
        void testJavaHome() {
            /*
             * Con la anotacion @EnabledIfEnvironmentVariable puedo habilitar o deshabilitar un test
             * dependiendo de una variable de ambiente del sistema
             */
        }
    }

    @Nested
    @DisplayName("Probando metodos relacionados con las exceptions")
    class CuentaExceptions {
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
    }

    @ParameterizedTest
    @ValueSource(strings = {"100", "200", "300", "500", "700", "1000"})
    @DisplayName("Probando el metodo debito de la cuenta parametrizado")
    void testDebitoCuentaParametrizado(String monto) {
        /*
         * El test parametrizado sirve para ejecutar el mismo test con diferentes parametros
         * en este caso ejecuto el test con los montos 100, 200, 300, 500, 700 y 1000
         *
         * Los valores se pueden pasar desde la anotacion @ValueSource o desde un metodo
         * que devuelva un Stream, por ejemplo un archivo CSV
         */
        cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo(), () -> "El saldo no puede ser nulo");
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, () -> "El saldo no es el esperado");
    }

    @Nested
    @DisplayName("Probando metodos relacionados con el tiempo")
    class TimeOutTest {
        @Test
        @DisplayName("Probando el timeout")
        @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
            // 500 milisegundos
        void testTimeOut1() throws InterruptedException {
            TimeUnit.SECONDS.sleep(2);
        }

        @Test
        @DisplayName("Probando el timeout")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
            // 2 segundos
        void testTimeOut2() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Test
        void testTimeOutAssertion() {
            /*
             * Si el numero es mayor al primer parametro del metodo assertTimeout()
             * el metodo falla
             */
            assertTimeout(Duration.ofSeconds(2), () -> {
                TimeUnit.SECONDS.sleep(1);
            });
        }
    }

}