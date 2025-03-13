package com.codigo.msregisterhexagonal.infraestructure.repository;

import com.codigo.msregisterhexagonal.infraestructure.entity.PersonEntityDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepositoryDoc extends MongoRepository<PersonEntityDoc, Long> {
}
