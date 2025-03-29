package com.codigo.patron_builder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Carro {
    private Long id;
    private String modelo;
    private Integer anio;
    private String marca;
    private TipoCarro tipo;

}
