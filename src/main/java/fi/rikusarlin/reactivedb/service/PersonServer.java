package fi.rikusarlin.reactivedb.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.rikusarlin.reactivedb.exception.InvalidPersonException;
import fi.rikusarlin.reactivedb.handler.PersonHandler;
import fi.rikusarlin.reactivedb.model.Person;
import fi.rikusarlin.reactivedb.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PersonServer {

	
	private final PersonRepository personRepository;
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	static Logger logger = LoggerFactory.getLogger(PersonHandler.class);

	public PersonServer(PersonRepository repository) {
		this.personRepository = repository;
	}
	
	public Flux<Person> findAllPersons() {
		return personRepository.findAll();
	}

	public Mono<Person> findById(UUID id) {
		return personRepository.findById(id);
	}

	public Flux<Person> findByLastName(String lastName) {
		return personRepository.findByLastName(lastName);
	}

	public Mono<Person> createNewPerson(Person newPerson, String principalName) {
		newPerson.setCreated(LocalDateTime.now());
		newPerson.setCreator(principalName);
		newPerson.setModified(LocalDateTime.now());
		newPerson.setModifier(principalName);

		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Person>> violations = validator.validate(newPerson);
		if(!violations.isEmpty()) {
			return Mono.error(new InvalidPersonException(violations));
		}

		return Mono.just(newPerson)
				.flatMap(personRepository::save)
				.flatMap(data -> personRepository.findById(data.getId()));
	}

	public Mono<Person> updatePerson(Person updatedPerson, String principalName) {
		updatedPerson.setModified(LocalDateTime.now());
		updatedPerson.setModifier(principalName);

		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Person>> violations = validator.validate(updatedPerson);
		if(!violations.isEmpty()) {
			return Mono.error(new InvalidPersonException(violations));
		}

		return Mono.just(updatedPerson)
				.flatMap(personRepository::save);
	}

	public Mono<Object> deletePerson(UUID id) {
		return personRepository.findById(id)
				.map(person -> personRepository.delete(person));
	}
}
