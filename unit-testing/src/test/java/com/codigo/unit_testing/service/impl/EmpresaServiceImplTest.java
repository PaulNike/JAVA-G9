package com.codigo.unit_testing.service.impl;

import com.codigo.unit_testing.aggregates.constants.Constants;
import com.codigo.unit_testing.aggregates.request.EmpresaRequest;
import com.codigo.unit_testing.aggregates.response.BaseResponse;
import com.codigo.unit_testing.dao.EmpresaRepository;
import com.codigo.unit_testing.entity.Empresa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class EmpresaServiceImplTest {

    @Mock
    private EmpresaRepository empresaRepository;
    @InjectMocks
    private EmpresaServiceImpl empresaServiceImpl;

    private Empresa empresa;
    private EmpresaRequest empresaRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //Inicializa los MOCKS antes de cada prueba
        empresa = new Empresa();
        empresaRequest = new EmpresaRequest();
        empresaRequest.setNumeroDocumento("123456789");
    }

    @Test
    void testCrearEmpresaExiste(){
        //ARRANGE
        when(empresaRepository.existsByNumeroDocumento(anyString())).thenReturn(true);

        //ACT
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaServiceImpl.crear(empresaRequest);

        //ASSERT
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_EXIST, resultado.getBody().getCode(),
                "Codigo recibido es diferente al esperado");
    }

    @Test
    void testCrearEmpresaNueva(){
        //ARRANGE
        when(empresaRepository.existsByNumeroDocumento(anyString())).thenReturn(false);
        when(empresaRepository.save(any())).thenReturn(empresa);

        //ACT -> EJECUTAR EL SERVICIO
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaServiceImpl.crear(empresaRequest);

        //ASSERT
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertEquals(Constants.MSJ_OK, resultado.getBody().getMessage());
        assertTrue(resultado.getBody().getObjeto().isPresent());
        assertSame(empresa, resultado.getBody().getObjeto().get());

    }

    @Test
    void testObtenerEmpresaHappyPath(){

        //ARRANGE
        when(empresaRepository.findById(any())).thenReturn(Optional.of(empresa));
        //ACT
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaServiceImpl.obtenerEmpresa(1L);
        //ASSERT
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertEquals(Constants.MSJ_OK, resultado.getBody().getMessage());
        assertTrue(resultado.getBody().getObjeto().isPresent());
        assertSame(empresa, resultado.getBody().getObjeto().get());

    }
    @Test
    void testObtenerEmpresaNoExisteHappyPath(){
        //ARRANGE
        //Definimos el comportamiento de nuestro mock
        when(empresaRepository.findById(any())).thenReturn(Optional.empty());

        //ACT
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaServiceImpl.obtenerEmpresa(1L);

        //ASSERT
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST,resultado.getBody().getCode());
        assertEquals(Constants.MSJ_EMPRESA_NO_EXIST,resultado.getBody().getMessage());
        assertFalse(resultado.getBody().getObjeto().isPresent());
    }

    @Test
    void testActualziarEmpresaHappyPath(){
        //arrange
        Long id = 1L;
        when(empresaRepository.existsById(id)).thenReturn(true);
        when(empresaRepository.findById(id)).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any())).thenReturn(empresa);

        //act
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaServiceImpl.actualizar(id,empresaRequest);

        //assert
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_OK,resultado.getBody().getCode());
        assertEquals(Constants.MSJ_OK,resultado.getBody().getMessage());
        assertTrue(resultado.getBody().getObjeto().isPresent());
        assertSame(empresa, resultado.getBody().getObjeto().get());
    }
    @Test
    void testActualizarEmpresaNoExistenteLanzaExcepcion() {
        Long id = 1L;
        when(empresaRepository.existsById(id)).thenReturn(true);
        when(empresaRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaServiceImpl.actualizar(id, empresaRequest);
        });

        assertEquals("Error Empresa no encontrada", exception.getMessage());
    }
    @Test
    void testActualziarEmpresaNoExisteHappyPath(){
        //arrange
        Long id = 1L;
        when(empresaRepository.existsById(id)).thenReturn(false);

        //act
        ResponseEntity<BaseResponse<Empresa>> resultado = empresaServiceImpl.actualizar(id,empresaRequest);

        //assert
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST,resultado.getBody().getCode());
        assertEquals(Constants.MSJ_EMPRESA_NO_EXIST,resultado.getBody().getMessage());
        assertFalse(resultado.getBody().getObjeto().isPresent());

    }
    @Test
    void testObtenerTodosEmpresas() {
        List<Empresa> listaEmpresas = List.of(empresa);
        when(empresaRepository.findAll()).thenReturn(listaEmpresas);

        ResponseEntity<BaseResponse<List<Empresa>>> resultado = empresaServiceImpl.obtenerTodos();

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertEquals(Constants.MSJ_OK, resultado.getBody().getMessage());
        assertTrue(resultado.getBody().getObjeto().isPresent());
        assertEquals(1, resultado.getBody().getObjeto().get().size());
    }

    @Test
    void testObtenerTodosEmpresasVacio() {
        when(empresaRepository.findAll()).thenReturn(List.of());

        ResponseEntity<BaseResponse<List<Empresa>>> resultado = empresaServiceImpl.obtenerTodos();

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, resultado.getBody().getCode());
        assertEquals(Constants.MSJ_EMPRESA_NO_EXIST, resultado.getBody().getMessage());
        assertFalse(resultado.getBody().getObjeto().isPresent());
    }

    @Test
    void testEliminarEmpresaExistente() {
        Long id = 1L;
        when(empresaRepository.existsById(id)).thenReturn(true);
        when(empresaRepository.findById(id)).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any())).thenReturn(empresa);

        ResponseEntity<BaseResponse<Empresa>> resultado = empresaServiceImpl.delete(id);

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_OK, resultado.getBody().getCode());
        assertEquals(Constants.MSJ_OK, resultado.getBody().getMessage());
        assertTrue(resultado.getBody().getObjeto().isPresent());
        assertSame(empresa, resultado.getBody().getObjeto().get());
    }

    @Test
    void testEliminarEmpresaNoExistente() {
        Long id = 1L;
        when(empresaRepository.existsById(id)).thenReturn(false);

        ResponseEntity<BaseResponse<Empresa>> resultado = empresaServiceImpl.delete(id);

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(Constants.CODE_EMPRESA_NO_EXIST, resultado.getBody().getCode());
        assertEquals(Constants.MSJ_EMPRESA_NO_EXIST, resultado.getBody().getMessage());
        assertFalse(resultado.getBody().getObjeto().isPresent());
    }

}