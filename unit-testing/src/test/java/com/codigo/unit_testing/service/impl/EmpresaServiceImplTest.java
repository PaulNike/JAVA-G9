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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

}