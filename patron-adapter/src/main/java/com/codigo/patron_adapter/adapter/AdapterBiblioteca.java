package com.codigo.patron_adapter.adapter;

import com.codigo.patron_adapter.antiguo.Biblioteca;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Component
//@Service
//@Repository
//@Controller
//@RestController
public class AdapterBiblioteca {

    private final Biblioteca biblioteca = new Biblioteca();

    public String obtenerDetalle(int idLibro){
        //Obtener el detalle del XML
        String detalle = biblioteca.obtenerInformacion(idLibro);
        //Adaptar el XML a JSON
        JSONObject jsonObject = XML.toJSONObject(detalle);
        return jsonObject.toString();
    }
}
