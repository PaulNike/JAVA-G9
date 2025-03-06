package com.codigo.unit_testing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraServiceTest {

    private CalculadoraService service;

    @BeforeEach
    void setUp() {
        service = new CalculadoraService();
    }

    @Test
    void testSumarHappyPath(){
        int resultado = service.sumar(2,3);
        assertEquals(5,resultado);
    }
    @Test
    void testRestarHappyPath(){
        int resultado = service.restar(2,3);
        assertEquals(-1,resultado);
    }
    @Test
    void testDividirHappyPath(){
        int resultado = service.dividir(10,2);
        assertEquals(5,resultado);
    }

    @Test
    void testDividirErrorTesting(){
        assertThrows(ArithmeticException.class, () ->
                service.dividir(10,0));
    }

    @Test
    void testEsParHappyPath(){
        assertTrue(service.esPar(2));
    }


    @Test
    void testNoEsParHappyPath(){
        assertFalse(service.esPar(1));
    }

    @Test
    void testEstaEnRangoHappyPath(){
        assertTrue(service.estaEnRango(5,1,10));
    }
    @Test
    void testNoEstaEnRangoHappyPath(){
        assertFalse(service.estaEnRango(2,5,10));
    }
    @Test
    void testObtenerNombreHappyPath(){
        String nombreObtenido = service.obtenerNombre("Paul");
        assertEquals("PAUL",nombreObtenido);
    }


}