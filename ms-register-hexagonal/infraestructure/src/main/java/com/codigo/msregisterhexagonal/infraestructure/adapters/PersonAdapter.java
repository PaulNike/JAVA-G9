package com.codigo.msregisterhexagonal.infraestructure.adapters;

import com.codigo.msregisterhexagonal.domain.aggregates.dto.PersonDTO;
import com.codigo.msregisterhexagonal.domain.ports.out.PersonServiceOut;
import com.codigo.msregisterhexagonal.infraestructure.entity.PersonEntity;
import com.codigo.msregisterhexagonal.infraestructure.entity.PersonEntityDoc;
import com.codigo.msregisterhexagonal.infraestructure.repository.PersonRepository;
import com.codigo.msregisterhexagonal.infraestructure.repository.PersonRepositoryDoc;
import com.codigo.msregisterhexagonal.infraestructure.response.ResponseReniec;
import com.codigo.msregisterhexagonal.infraestructure.rest.ReniecClient;
import com.codigo.msregisterhexagonal.infraestructure.retrofit.ClientReniecServiceRetrofit;
import com.codigo.msregisterhexagonal.infraestructure.retrofit.ClienteReniecRetrofit;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Response;


import java.io.IOException;
import java.net.http.HttpClient;
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
    private final RestTemplate restTemplate;

    ClienteReniecRetrofit retrofitPreConfig =  ClientReniecServiceRetrofit
            .getNewRetrofit()
            .create(ClienteReniecRetrofit.class);

    @Value("${token}")
    private String token;

    public PersonAdapter(PersonRepository personRepository,
                         PersonRepositoryDoc personRepositoryDoc, ReniecClient reniecClient,
                         @Qualifier("defaulMapper") ModelMapper personMapper,
                         @Qualifier("reniecMapper") ModelMapper reniecMapper,
                         @Qualifier("reniecMapperDoc") ModelMapper reniecMapperDoc, RestTemplate restTemplate) {
        this.personRepository = personRepository;
        this.personRepositoryDoc = personRepositoryDoc;
        this.reniecClient = reniecClient;
        this.personMapper = personMapper;
        this.reniecMapper = reniecMapper;
        this.reniecMapperDoc = reniecMapperDoc;
        this.restTemplate = restTemplate;
    }

    @Override
    public PersonDTO createPersonOut(String dni) throws IOException {
        log.info("Inicio - createPersonOut - DNI: {}", dni);

        return Optional.of(getEntity(dni))
                .map(personRepositoryDoc::save)
                .map(this::mapToDtoDoc)
                .orElseThrow(() -> new RuntimeException("ERROR AL GUARDAR LA PERSONA CON DNI: " + dni));
    }

    private PersonEntityDoc getEntity(String dni) throws IOException {
        log.info("GetEntity para dni: {}", dni);
        //ResponseReniec responseReniec = executeReniec(dni);  //OPENFEIGN
        //ResponseReniec responseReniec = executeReniecRetrofit(dni);  //RETROFIT
        ResponseReniec responseReniec = executeRestTemplate(dni);  //RESTTEMPLATE

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

    //RETROFIT
    private ResponseReniec executeReniecRetrofit(String dni) throws IOException {
        ResponseReniec responseReniec = new ResponseReniec();
        //AQUI SE EJECUTA LA SOLICITUDES DE RENIEC execute()
        Response<ResponseReniec> executeReniec = preparacionRetrofitParams(dni).execute();

        //VALIDAR RESUTLADOS:
        if(executeReniec.isSuccessful() && executeReniec.body() != null){
            responseReniec = executeReniec.body();
        }
        return responseReniec;

    }
    //METODO DE APOYO PARA PREPARAR EL CLIENTE RETROFIT CON LOS PARAMETROS
    private Call<ResponseReniec> preparacionRetrofitParams(String dni){
        return retrofitPreConfig.getInfoReniec(token, dni);
    }

    //RESTEMPLATE
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+token);
     return headers;
    }

    private ResponseReniec executeRestTemplate(String dni){
        String url = "https://api.apis.net.pe/v2/reniec/dni?numero="+dni;

        try {
            HttpEntity<?> cabeceras = new HttpEntity<>(createHeaders());

            ResponseEntity<ResponseReniec> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    cabeceras,
                    ResponseReniec.class
            );

            //validar resultados
            if(response.getStatusCode() == org.springframework.http.HttpStatus.OK){
                return response.getBody();
            }

        }catch (Exception e){
            log.error("Error al consultar el servicio externo para el DNI: {}", dni, e);
        }
        return null;
    }

    }
