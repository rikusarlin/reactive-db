package fi.rikusarlin.reactivedb.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Component;

import fi.rikusarlin.reactivedb.model.Person;
import reactor.core.publisher.Flux;

@Component
public interface PersonRepository extends R2dbcRepository<Person, UUID> {
  Flux<Person> findByLastName(String lastName);
}
