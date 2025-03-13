package com.codigo.msregisterhexagonal.domain.ports.out;

import com.codigo.msregisterhexagonal.domain.aggregates.dto.PersonDTO;

import java.io.IOException;

public interface PersonServiceOut {
    PersonDTO createPersonOut(String dni) throws IOException;
}
