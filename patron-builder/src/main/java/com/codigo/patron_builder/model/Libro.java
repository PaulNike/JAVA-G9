package com.codigo.patron_builder.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Libro {
    private String titulo;
    private String autor;
    private String fechaPublicacion;
    private String isbn;
    private String estado;


    private Libro(Builder builder){
        this.titulo = builder.titulo;
        this.autor = builder.autor;
        this.fechaPublicacion = builder.fechaPublicacion;
        this.isbn = builder.isbn;
        this.estado = builder.estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getEstado() {
        return estado;
    }

    public static class Builder{
        private String titulo;
        private String autor;
        private String fechaPublicacion;
        private String isbn;
        private String estado;

        public Builder titulo(String titulo){
            this.titulo = titulo;
            return this;
        }
        public Builder autor(String autor){
            this.autor = autor;
            return this;
        }
        public Builder fechaPublicacion(String fechaPublicacion){
            this.fechaPublicacion = fechaPublicacion;
            return this;
        }
        public Builder isbn(String isbn){
            this.isbn = isbn;
            return this;
        }
        public Builder estado(String estado){
            this.estado = estado;
            return this;
        }

        public Libro build(){
            return new Libro(this);
        }

    }

}
