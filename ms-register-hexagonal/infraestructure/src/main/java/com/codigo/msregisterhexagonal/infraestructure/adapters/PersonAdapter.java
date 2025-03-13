package com.codigo.msregisterhexagonal.infraestructure.adapters;

import com.codigo.msregisterhexagonal.domain.aggregates.dto.PersonDTO;
import com.codigo.msregisterhexagonal.domain.ports.out.PersonServiceOut;
import com.codigo.msregisterhexagonal.infraestructure.entity.PersonEntity;
import com.codigo.msregisterhexagonal.infraestructure.entity.PersonEntityDoc;
import com.codigo.msregisterhexagonal.infraestructure.repository.PersonRepository;
import com.codigo.msregisterhexagonal.infraestructure.repository.PersonRepositoryDoc;
import com.codigo.msregisterhexagonal.infraestructure.response.ResponseReniec;
import com.codigo.msregisterhexagonal.infraestructure.rest.ReniecClient;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Log4j2
public class PersonAdapter implements PersonServiceOut {

    private final PersonRepository personRepository;
    private final PersonRepositoryDoc personRepositoryDoc;
    private final ReniecClient reniecClient;
    private final ModelMapper personMapper;
    private final ModelMapper reniecMapper;
    private final ModelMapper reniecMapperDoc;


    @Value("${token}")
    private String token;

    public PersonAdapter(PersonRepository personRepository,
                         PersonRepositoryDoc personRepositoryDoc, ReniecClient reniecClient,
                         @Qualifier("defaulMapper") ModelMapper personMapper,
                         @Qualifier("reniecMapper") ModelMapper reniecMapper,
                         @Qualifier("reniecMapperDoc") ModelMapper reniecMapperDoc) {
        this.personRepository = personRepository;
        this.personRepositoryDoc = personRepositoryDoc;
        this.reniecClient = reniecClient;
        this.personMapper = personMapper;
        this.reniecMapper = reniecMapper;
        this.reniecMapperDoc = reniecMapperDoc;
    }

    @Override
    public PersonDTO createPersonOut(String dni) {
        log.info("Inicio - createPersonOut - DNI: {}", dni);

        return Optional.of(getEntity(dni))
                .map(personRepositoryDoc::save)
                .map(this::mapToDtoDoc)
                .orElseThrow(() -> new RuntimeException("ERROR AL GUARDAR LA PERSONA CON DNI: " + dni));
    }

    private PersonEntityDoc getEntity(String dni){
        log.info("GetEntity para dni: {}", dni);
        ResponseReniec responseReniec = executeReniec(dni);

        if(responseReniec == null ||
            responseReniec.getNumeroDocumento() == null){
            throw new RuntimeException("Respuesta Invalida de RENIEC: "+ dni);
        }
        //Si todo pasa OK:
        //PersonEntity person = mapReniecToEntity(responseReniec);
        PersonEntityDoc person = reniecMapperDoc.map(responseReniec, PersonEntityDoc.class);
        //Agregar el resto de campos:
        person.setStatus(1);
        person.setUserCreate("PRODRIGUEZ");
        person.setDateCreate(new Timestamp(System.currentTimeMillis()));
        return person;
    }



    private ResponseReniec executeReniec(String dni){
        String header = "Barer "+ token;
        log.info("Consultando RENIEC para dni: {}", dni);

        return Optional.ofNullable(reniecClient.getInfoReniec(dni,header))
                .orElseThrow(() -> new RuntimeException("Error al consultar la persona"));
    }

    private PersonEntity mapReniecToEntity(ResponseReniec responseReniec){
        return reniecMapper.map(responseReniec, PersonEntity.class);
    }
    private PersonDTO mapToDto(PersonEntity personEntity){
        return personMapper.map(personEntity, PersonDTO.class);
    }
    private PersonDTO mapToDtoDoc(PersonEntityDoc personEntityDoc){
        return personMapper.map(personEntityDoc, PersonDTO.class);
    }
}
