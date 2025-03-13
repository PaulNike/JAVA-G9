package com.codigo.msregisterhexagonal.domain.ports.out;

import com.codigo.msregisterhexagonal.domain.aggregates.dto.PersonDTO;

public interface PersonServiceOut {
    PersonDTO createPersonOut(String dni);
}
