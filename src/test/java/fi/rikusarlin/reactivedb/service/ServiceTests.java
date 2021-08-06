package fi.rikusarlin.reactivedb.service;

import static org.mockito.Mockito.times;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import fi.rikusarlin.reactivedb.handler.PersonHandler;
import fi.rikusarlin.reactivedb.handler.Router;
import fi.rikusarlin.reactivedb.model.Person;
import fi.rikusarlin.reactivedb.repository.PersonRepository;
import fi.rikusarlin.reactivedb.testdata.PersonData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(PersonServer.class)
@Import({PersonHandler.class, Router.class})
public class ServiceTests 
{
    @MockBean
    PersonRepository repository;
    
    @Autowired
    private WebTestClient webClient; 

    @Test
    void testCreatePerson() {
		Person p1 = PersonData.getPerson1();
        Mockito.when(repository.save(Mockito.any(Person.class))).thenReturn(Mono.just(p1));
        Mockito.when(repository.findById(Mockito.any(UUID.class))).thenReturn(Mono.just(p1));
        this.webClient.post()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.lastName").isEqualTo("Wnape");
        Mockito.verify(repository, times(1)).save(Mockito.any(Person.class));
        Mockito.verify(repository, times(1)).findById(Mockito.any(UUID.class));
    }

    @Test
    void testCreatePerson_failsValidation() {
		Person p1 = PersonData.getPerson1();
		p1.setPersonNumber("010170-906X");	
		
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
        Mockito.verify(repository, times(0)).save(Mockito.any(Person.class));
        Mockito.verify(repository, times(0)).findById(Mockito.any(UUID.class));
    }

    @Test
    void testUpdatePerson() {
		Person p1 = PersonData.getPerson1();
        Mockito.when(repository.save(Mockito.any(Person.class))).thenReturn(Mono.just(p1));
        this.webClient.put()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.lastName").isEqualTo("Wnape");
        Mockito.verify(repository, times(1)).save(Mockito.any(Person.class));
    }

    @Test
    void testUpdatePerson_failsValidation() {
		Person p1 = PersonData.getPerson1();
		p1.setFirstName("012345678901234567890123456789");	
		
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
        Mockito.verify(repository, times(0)).save(Mockito.any(Person.class));
    }

    @Test
    void testGetPerson_Found() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Mono.just(p2));
        this.webClient.get()
            .uri("/persons/"+id.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.lastName").isEqualTo("Wnape");
        Mockito.verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetPerson_NotFound() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Mono.empty());
        this.webClient.get()
            .uri("/persons/"+id.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
        Mockito.verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetPerson_IllegalUUID() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        this.webClient.get()
            .uri("/persons/"+id.toString()+"0123456789")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest();
        Mockito.verify(repository, times(0)).findById(id);
    }

    @Test
    void testGetAll() {
		Person[] persons = new Person[2];
		persons[0] = PersonData.getPerson1();
		persons[1] = PersonData.getPerson2();

        Mockito.when(repository.findAll()).thenReturn(Flux.fromArray(persons));
        this.webClient.get()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.length()").isEqualTo(2);
        Mockito.verify(repository, times(1)).findAll();
    }

    @Test
    void testGetByLastName() {
		Person[] persons = new Person[3];
		persons[0] = PersonData.getPerson1();
		persons[1] = PersonData.getPerson2();
		persons[2] = PersonData.getPerson3();

        Mockito.when(repository.findByLastName("Wnape")).thenReturn(Flux.fromArray(persons));
        this.webClient.get()
            .uri("/persons/byLastName/Wnape")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.length()").isEqualTo(3);
        Mockito.verify(repository, times(1)).findByLastName("Wnape");
    }

    @Test
    void testGetByLastName_empty() {
        Mockito.when(repository.findByLastName("Wonka")).thenReturn(Flux.empty());

        this.webClient.get()
            .uri("/persons/byLastName/Wonka")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();
        Mockito.verify(repository, times(1)).findByLastName("Wonka");
    }

    @Test
    void testDeletePerson_Found() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(repository.deleteById(id)).thenReturn(Mono.empty());
        this.webClient.delete()
            .uri("/persons/"+id.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
        Mockito.verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDeletePerson_illegalUUID() {
    	UUID id = UUID.randomUUID();
		Person p2 = PersonData.getPerson1();
		p2.setId(id);

        Mockito.when(repository.deleteById(id)).thenReturn(Mono.empty());
        this.webClient.delete()
            .uri("/persons/"+id.toString()+"123456789")
            .exchange()
            .expectStatus().isBadRequest();
        Mockito.verify(repository, times(0)).deleteById(id);
    }
}