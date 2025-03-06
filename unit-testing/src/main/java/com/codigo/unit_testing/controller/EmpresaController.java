package com.codigo.unit_testing.controller;


import com.codigo.unit_testing.aggregates.request.EmpresaRequest;
import com.codigo.unit_testing.aggregates.response.BaseResponse;
import com.codigo.unit_testing.entity.Empresa;
import com.codigo.unit_testing.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/empresa/v1/")
@RequiredArgsConstructor
public class EmpresaController {


    private final EmpresaService service;


    @PostMapping
    public ResponseEntity<BaseResponse<Empresa>> registrar(
            @RequestBody EmpresaRequest empresaRequest){
        return service.crear(empresaRequest);
    }
    @PostMapping("/registrar2")
    public ResponseEntity<BaseResponse> registrar2(
            @RequestBody String dato){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(2000);
        baseResponse.setMessage("TODO OK");
        baseResponse.setObjeto(Optional.of(dato));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Empresa>>obtenerUno(
            @PathVariable Long id){
        return service.obtenerEmpresa(id);
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<List<Empresa>>>obtenerTodos(){
        return service.obtenerTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Empresa>>actualizar(@PathVariable Long id, @RequestBody EmpresaRequest request){
        return service.actualizar(id,request);

    }
}
