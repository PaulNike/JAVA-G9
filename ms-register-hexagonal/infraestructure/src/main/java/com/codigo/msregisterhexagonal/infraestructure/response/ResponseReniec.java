package com.codigo.msregisterhexagonal.infraestructure.response;

import lombok.Data;

@Data
public class ResponseReniec {
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String digitoVerificador;

    //XRAY -> Gestión de pruebas para QA(Test Managment)
    //JFROG Xray -> Seguridad y analisis de vulnerabilidades de dependencias
}
