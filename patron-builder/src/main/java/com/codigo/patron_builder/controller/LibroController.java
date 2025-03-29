package com.codigo.patron_builder.controller;

import com.codigo.patron_builder.model.Carro;
import com.codigo.patron_builder.model.Libro;
import com.codigo.patron_builder.model.TipoCarro;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/v1/builder")
public class LibroController {

    @GetMapping("/librofull")
    public Libro obtenerLibroManual(){
        return new Libro.Builder()
                //.titulo("EL Conde de Montecristo")
                //.autor("Alexandre Dumas")
                //.fechaPublicacion("25/05/2005")
                //.isbn("xxxxxxxxxx")
                //.estado("ACTIVO")
                .build();
    }

    @GetMapping("/carrofull")
    public Carro obtenerCarroManual(){
        return Carro.builder()
                .id(1L)
                .marca("GEELY")
                .modelo("SUV")
                .anio(2025)
                .tipo(TipoCarro.builder()
                        .id(2L)
                        .tipo("AUTOMATICO")
                        .build())
                .build();
    }
}
