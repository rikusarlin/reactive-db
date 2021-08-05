package fi.rikusarlin.reactivedb.service;

import static org.mockito.Mockito.times;

import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import fi.rikusarlin.reactivedb.exception.InvalidPersonException;
import fi.rikusarlin.reactivedb.model.Person;
import fi.rikusarlin.reactivedb.testdata.PersonData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest
public class HandlerTest 
{
    @MockBean
    PersonServer personServer;
    
    @Autowired
    private WebTestClient webClient; 
 
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    
    @Test
    void testCreatePerson() {
		Person p1 = PersonData.getPerson1();
        Mockito.when(personServer.createNewPerson(Mockito.any(Person.class), Mockito.anyString())).thenReturn(Mono.just(p1));
        this.webClient.post()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.lastName").isEqualTo("Wnape");
    }

    @Test
    void testCreatePerson_failsValidation() {
		Person p1 = PersonData.getPerson1();
		p1.setPersonNumber("010170-906X");	
		
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Person>> violations = validator.validate(p1);	
		InvalidPersonException ipe = new InvalidPersonException();
		ipe.setViolations(violations);
				
        Mockito.when(personServer.createNewPerson(Mockito.any(Person.class), Mockito.anyString())).thenReturn(Mono.error(ipe));
        this.webClient.post()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$..field").isEqualTo("personNumber")
            .jsonPath("$..message").isEqualTo("invalid person number '010170-906X'");
    }

    @Test
    void testCreatePerson_serverError() {
		Person p1 = PersonData.getPerson1();
		p1.setPersonNumber("010170-906X");	
		
		NullPointerException npe = new NullPointerException("A sudden NPE, sorry");
		
        Mockito.when(personServer.createNewPerson(Mockito.any(Person.class), Mockito.anyString())).thenReturn(Mono.error(npe));
        this.webClient.post()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    void testUpdatePerson() {
		Person p1 = PersonData.getPerson1();
        Mockito.when(personServer.updatePerson(Mockito.any(Person.class), Mockito.anyString())).thenReturn(Mono.just(p1));
        this.webClient.put()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.lastName").isEqualTo("Wnape");
    }

    @Test
    void testUpdatePerson_failsValidation() {
		Person p1 = PersonData.getPerson1();
		p1.setFirstName("012345678901234567890123456789");	
		
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Person>> violations = validator.validate(p1);	
		InvalidPersonException ipe = new InvalidPersonException();
		ipe.setViolations(violations);
				
        Mockito.when(personServer.updatePerson(Mockito.any(Person.class), Mockito.anyString())).thenReturn(Mono.error(ipe));
        this.webClient.put()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$..field").isEqualTo("firstName")
            .jsonPath("$..message").isEqualTo("size must be between 0 and 20");
    }

    @Test
    void testUpdatePerson_notFound() {
		Person p1 = PersonData.getPerson1();
		
        Mockito.when(personServer.updatePerson(Mockito.any(Person.class), Mockito.anyString())).thenReturn(Mono.empty());
        this.webClient.put()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void testGetPerson_Found() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(personServer.findById(id)).thenReturn(Mono.just(p2));
        this.webClient.get()
            .uri("/persons/"+id.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody();
        Mockito.verify(personServer, times(1)).findById(id);
    }

    @Test
    void testGetPerson_NotFound() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(personServer.findById(id)).thenReturn(Mono.empty());
        this.webClient.get()
            .uri("/persons/"+id.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
        Mockito.verify(personServer, times(1)).findById(id);
    }

    @Test
    void testGetPerson_IllegalUUID() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(personServer.findById(id)).thenReturn(Mono.empty());
        this.webClient.get()
            .uri("/persons/"+id.toString()+"0123456789")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest();
        Mockito.verify(personServer, times(0)).findById(id);
    }

    @Test
    void testGetAll() {
		Person[] persons = new Person[2];
		persons[0] = PersonData.getPerson1();
		persons[1] = PersonData.getPerson2();

        Mockito.when(personServer.findAllPersons()).thenReturn(Flux.fromArray(persons));
        this.webClient.get()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.length()").isEqualTo(2);
        Mockito.verify(personServer, times(1)).findAllPersons();
    }

    @Test
    void testGetByLastName() {
		Person[] persons = new Person[3];
		persons[0] = PersonData.getPerson1();
		persons[1] = PersonData.getPerson2();
		persons[2] = PersonData.getPerson3();

        Mockito.when(personServer.findByLastName("Wnape")).thenReturn(Flux.fromArray(persons));
        this.webClient.get()
            .uri("/persons/byLastName/Wnape")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.length()").isEqualTo(3);
        Mockito.verify(personServer, times(1)).findByLastName("Wnape");
    }

    @Test
    void testGetByLastName_empty() {
        Mockito.when(personServer.findByLastName("Wonka")).thenReturn(Flux.empty());

        this.webClient.get()
            .uri("/persons/byLastName/Wonka")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();
        Mockito.verify(personServer, times(1)).findByLastName("Wonka");
    }

    @Test
    void testDeletePerson_Found() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(personServer.deletePerson(id)).thenReturn(Mono.empty());
        this.webClient.delete()
            .uri("/persons/"+id.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
        Mockito.verify(personServer, times(1)).deletePerson(id);
    }

    @Test
    void testDeletePerson_illegalUUID() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(personServer.deletePerson(id)).thenReturn(Mono.empty());
        this.webClient.delete()
            .uri("/persons/"+id.toString()+"123456789")
            .exchange()
            .expectStatus().isBadRequest();
        Mockito.verify(personServer, times(0)).deletePerson(id);
    }

}