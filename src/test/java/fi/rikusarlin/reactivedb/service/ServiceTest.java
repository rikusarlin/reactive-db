package fi.rikusarlin.reactivedb.service;

import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import fi.rikusarlin.reactivedb.model.Gender;
import fi.rikusarlin.reactivedb.model.Person;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest
public class ServiceTest 
{
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    @MockBean
    PersonServer personServer;
    
    @Autowired
    private WebTestClient webClient; 
 
    /*
    @Test
    void testCreatePerson() {
    	UUID id = UUID.randomUUID();
    	String userName = "User";
		Person p1 = Person.builder()
				.personNumber("010170-901K")
				.firstName("Rauli")
				.lastName("Wnape")
				.birthDate(LocalDate.parse("01.01.1970", formatter))
				.gender(Gender.MALE)
				.email("rauliwnape2@hotmail.com")
				.build();
		Person p2 = Person.builder()
				.id(id)
				.personNumber("010170-901P")
				.firstName("Rauli")
				.lastName("Wnape")
				.birthDate(LocalDate.parse("01.01.1970", formatter))
				.gender(Gender.MALE)
				.email("rauliwnape2@hotmail.com")
				.created(LocalDateTime.now())
				.creator(userName)
				.modified(LocalDateTime.now())
				.modifier(userName)
				.build();
		
        Mockito.when(personServer.createNewPerson(p1, userName)).thenReturn(Mono.just(p2));
        Mockito.when(personServer.findById(id)).thenReturn(Mono.just(p2));
        this.webClient.post()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(p1),Person.class)
            .exchange()
            .expectStatus().isOk();
        Mockito.verify(personServer, times(1)).createNewPerson(p1, userName);
        Mockito.verify(personServer, times(1)).findById(id);
    }
    */
    
    @Test
    void testGetPerson_Found() {
    	UUID id = UUID.randomUUID();
    	String userName = "User";
		Person p2 = Person.builder()
				.id(id)
				.personNumber("010170-901K")
				.firstName("Rauli")
				.lastName("Wnape")
				.birthDate(LocalDate.parse("01.01.1970", formatter))
				.gender(Gender.MALE)
				.email("rauliwnape2@hotmail.com")
				.created(LocalDateTime.now())
				.creator(userName)
				.modified(LocalDateTime.now())
				.modifier(userName)
				.build();

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
}