package com.codigo.patron_adapter.antiguo;

public class Biblioteca {

    public String obtenerInformacion(int idLibro){
        return "<libro>\n" +
                "    <id>" + idLibro +"</id>\n" +
                "    <titulo>Maria</titulo>\n" +
                "    <editorial>Ruben Editorial</editorial>\n" +
                "    <anio>1996</anio>\n" +
                "    <ciudad>Colombia</ciudad>\n" +
                "    <direccion>123 Calle Ficticia, Ciudad Ejemplo</direccion>\n" +
                "    <email>ruben.editorial@example.com</email>\n" +
                "    <telefono>1234567890</telefono>\n" +
                "    <genero>Terror</genero>\n" +
                "    <isbn>xxxxx-xxxx-xxx</isbn>\n" +
                "    <nacionalidad>Española</nacionalidad>\n" +
                "</libro>\n";
    }
}
