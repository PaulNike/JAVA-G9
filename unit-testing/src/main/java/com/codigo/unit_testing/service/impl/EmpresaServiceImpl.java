package com.codigo.unit_testing.service.impl;


import com.codigo.unit_testing.aggregates.constants.Constants;
import com.codigo.unit_testing.aggregates.request.EmpresaRequest;
import com.codigo.unit_testing.aggregates.response.BaseResponse;
import com.codigo.unit_testing.dao.EmpresaRepository;
import com.codigo.unit_testing.entity.Empresa;
import com.codigo.unit_testing.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    @Override
    public ResponseEntity<BaseResponse<Empresa>> crear(EmpresaRequest request) {
        BaseResponse<Empresa> baseResponse = new BaseResponse<>();
        boolean exist =
                empresaRepository.existsByNumeroDocumento(request.getNumeroDocumento());
        if(exist){
            baseResponse.setCode(Constants.CODE_EXIST);
            baseResponse.setMessage(Constants.MSJ_EXIST);
            baseResponse.setObjeto(Optional.empty());
            return ResponseEntity.ofNullable(baseResponse);

        }else{
            Empresa empresaGuardar = empresaRepository.save(getEntity(request));
            baseResponse.setCode(Constants.CODE_OK);
            baseResponse.setMessage(Constants.MSJ_OK);
            baseResponse.setObjeto(Optional.of(empresaGuardar));
            return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<BaseResponse<Empresa>> obtenerEmpresa(Long id) {
        BaseResponse<Empresa> baseResponse = new BaseResponse<>();
        Optional<Empresa> empresaBuscar = empresaRepository.findById(id);
        if(empresaBuscar.isPresent()){
            baseResponse.setCode(Constants.CODE_OK);
            baseResponse.setMessage(Constants.MSJ_OK);
            baseResponse.setObjeto(empresaBuscar);
        }else{
            //Mensaje informativo que no hay datos
            baseResponse.setCode(Constants.CODE_EMPRESA_NO_EXIST);
            baseResponse.setMessage(Constants.MSJ_EMPRESA_NO_EXIST);
            baseResponse.setObjeto(Optional.empty());
        }
        return ResponseEntity.ok(baseResponse);
    }

    @Override
    public ResponseEntity<BaseResponse<List<Empresa>>> obtenerTodos() {
        BaseResponse<List<Empresa>> baseResponse = new BaseResponse<>();
        List<Empresa> listEmpresa = empresaRepository.findAll();

        if(!listEmpresa.isEmpty()){
            baseResponse.setCode(Constants.CODE_OK);
            baseResponse.setMessage(Constants.MSJ_OK);
            baseResponse.setObjeto(Optional.of(listEmpresa));
        }else {
            baseResponse.setCode(Constants.CODE_EMPRESA_NO_EXIST);
            baseResponse.setMessage(Constants.MSJ_EMPRESA_NO_EXIST);
            baseResponse.setObjeto(Optional.empty());
        }
        return ResponseEntity.ok(baseResponse);
    }

    @Override
    public ResponseEntity<BaseResponse<Empresa>> actualizar(Long id, EmpresaRequest empresaRequest) {
        BaseResponse<Empresa> baseResponse = new BaseResponse<>();
        if (empresaRepository.existsById(id)){
            Empresa empresaRecuperada = empresaRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Error Empresa no encontrada"));
            Empresa actualizar = getEntityUpdate(empresaRequest,empresaRecuperada);
            baseResponse.setCode(Constants.CODE_OK);
            baseResponse.setMessage(Constants.MSJ_OK);
            assert actualizar != null;
            baseResponse.setObjeto(Optional.of(empresaRepository.save(actualizar)));
        }else{
            baseResponse.setCode(Constants.CODE_EMPRESA_NO_EXIST);
            baseResponse.setMessage(Constants.MSJ_EMPRESA_NO_EXIST);
            baseResponse.setObjeto(Optional.empty());
        }
        return ResponseEntity.ok(baseResponse);
    }

    @Override
    public ResponseEntity<BaseResponse<Empresa>> delete(Long id) {
        BaseResponse<Empresa> baseResponse = new BaseResponse<>();
        if (empresaRepository.existsById(id)){
            Empresa empresaRecuperada = empresaRepository.findById(id).orElse( null);
            empresaRecuperada.setEstado(0);
            empresaRecuperada.setUsuaDelet(Constants.AUDIT_ADMIN);
            empresaRecuperada.setDateDelet(getTimestamp());

            //Response
            baseResponse.setCode(Constants.CODE_OK);
            baseResponse.setMessage(Constants.MSJ_OK);
            baseResponse.setObjeto(Optional.of(empresaRepository.save(empresaRecuperada)));
        }else{
            baseResponse.setCode(Constants.CODE_EMPRESA_NO_EXIST);
            baseResponse.setMessage(Constants.MSJ_EMPRESA_NO_EXIST);
            baseResponse.setObjeto(Optional.empty());
        }
        return ResponseEntity.ok(baseResponse);
    }
    @Override
    public ResponseEntity<BaseResponse<Empresa>> obtenerEmpresaXNumDoc(String numDocu) {
        return null;
    }

    private Empresa getEntity(EmpresaRequest request){
        Empresa entity = new Empresa();
        entity.setRazonSocial(request.getRazonSocial());
        entity.setTipoDocumento(request.getTipoDocumento());
        entity.setNumeroDocumento(request.getNumeroDocumento());
        entity.setCondicion(Constants.CONDICION);
        entity.setDireccion(request.getDireccion());
        entity.setDistrito(request.getDistrito());
        entity.setProvincia(request.getProvincia());
        entity.setDepartamento(request.getDepartamento());
        entity.setEstado(Constants.STATUS_ACTIVE);
        entity.setEsAgenteRetencion(Constants.AGENTE_RETENCION_TRUE);
        entity.setUsuaCrea(Constants.AUDIT_ADMIN);
        entity.setDateCreate(getTimestamp());
        return entity;
    }
    private Timestamp getTimestamp(){
        long currentTime = System.currentTimeMillis();
        return new Timestamp(currentTime);
    }
    private Empresa getEntityUpdate(EmpresaRequest request, Empresa empresaRecuperada){
        if(empresaRecuperada != null){
            empresaRecuperada.setRazonSocial(request.getRazonSocial());
            empresaRecuperada.setTipoDocumento(request.getTipoDocumento());
            empresaRecuperada.setNumeroDocumento(request.getNumeroDocumento());
            empresaRecuperada.setDireccion(request.getDireccion());
            empresaRecuperada.setDistrito(request.getDistrito());
            empresaRecuperada.setProvincia(request.getProvincia());
            empresaRecuperada.setDepartamento(request.getDepartamento());
            empresaRecuperada.setUsuaModif(Constants.AUDIT_ADMIN);
            empresaRecuperada.setDateModif(getTimestamp());
        }else {
            return null;
        }
        return empresaRecuperada;
    }
}
