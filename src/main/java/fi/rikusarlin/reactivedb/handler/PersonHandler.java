package fi.rikusarlin.reactivedb.handler;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import fi.rikusarlin.reactivedb.model.Person;
import fi.rikusarlin.reactivedb.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PersonHandler {

  private MediaType json = MediaType.APPLICATION_JSON;

  private final PersonRepository personRepository;

  public Mono<ServerResponse> getAll(ServerRequest request) {
	Flux<Person> persons = personRepository.findAll();
    return ServerResponse.ok().contentType(json).body(persons, Person.class);
  }

  public Mono<ServerResponse> getById(ServerRequest request) {
    return personRepository
    		.findById(UUID.fromString(request.pathVariable("id")))
    		.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data));
  }

  public Mono<ServerResponse> getByLastName(ServerRequest request) {
	  Flux<Person> persons = personRepository
	    		.findByLastName(request.pathVariable("lastName"));
	  return ServerResponse.ok().contentType(json).body(persons, Person.class);
	  }

  public Mono<ServerResponse> createPerson(ServerRequest request) {
    return request.bodyToMono(Person.class)
			.map(person -> {
				String principalName = "";
				if(request.principal().block() != null) {
					principalName= request.principal().block().getName();
				}
				person.setCreated(LocalDateTime.now());
				person.setModifier(principalName);
				person.setModified(LocalDateTime.now());
				person.setModifier(principalName);
				return person;
			})
    		.flatMap(personRepository::save)
    		.flatMap(data -> personRepository.findById(data.getId()))
    		.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data));
  }

  public Mono<ServerResponse> updatePerson(ServerRequest request) {
	return request.bodyToMono(Person.class)
			.map(person -> {
				String principalName = "";
				if(request.principal().block() != null) {
					principalName= request.principal().block().getName();
				}
				person.setModified(LocalDateTime.now());
				person.setModifier(principalName);
				return person;
				})
    		.flatMap(personRepository::save)
    		.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data));
  }

  public Mono<ServerResponse> deletePerson(ServerRequest request) {
	    return personRepository
	    		.deleteById(UUID.fromString(request.pathVariable("id")))
	    		.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data));
  }
}
