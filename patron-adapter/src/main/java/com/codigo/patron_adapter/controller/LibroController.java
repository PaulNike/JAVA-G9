package com.codigo.patron_adapter.controller;

import com.codigo.patron_adapter.adapter.AdapterBiblioteca;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/adapter")
public class LibroController {

    private final AdapterBiblioteca biblioteca;

    public LibroController(AdapterBiblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String obtenerLibro(@PathVariable Integer id){
        return biblioteca.obtenerDetalle(id);
    }
}
