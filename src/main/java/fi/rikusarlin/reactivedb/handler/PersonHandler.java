package fi.rikusarlin.reactivedb.handler;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import fi.rikusarlin.reactivedb.exception.ErrorInfo;
import fi.rikusarlin.reactivedb.exception.ExceptionUtils;
import fi.rikusarlin.reactivedb.exception.InvalidPersonException;
import fi.rikusarlin.reactivedb.model.Person;
import fi.rikusarlin.reactivedb.service.PersonServer;
import reactor.core.publisher.Mono;

@Component
@Controller
public class PersonHandler {

	static Logger logger = LoggerFactory.getLogger(PersonHandler.class);

	private MediaType json = MediaType.APPLICATION_JSON;

	private final PersonServer personServer;

	public PersonHandler(PersonServer server) {
		this.personServer = server;
	}
	
	public Mono<ServerResponse> getAll(ServerRequest request) {
		return personServer.findAllPersons().collectList()
				.doOnEach(person -> logger.debug(person.toString()))
				.flatMap(list -> ServerResponse.ok().contentType(json).bodyValue(list));
	}

	public Mono<ServerResponse> getById(ServerRequest request) {
		String idParam = request.pathVariable("id");
		try {
			return personServer
					.findById(UUID.fromString(idParam))
					.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data))
					.switchIfEmpty(ServerResponse.notFound().build());
		} catch (IllegalArgumentException iae) {
			Mono<ErrorInfo> errorInfo = Mono.just(new ErrorInfo("UUID", "Illegal UUID value " + idParam, LocalDateTime.now()));
			return ServerResponse.badRequest().contentType(json).body(errorInfo, ErrorInfo.class);
		}
	}

	public Mono<ServerResponse> getByLastName(ServerRequest request) {
		return personServer.findByLastName(request.pathVariable("lastName")).collectList()
				.flatMap(list -> list.isEmpty() ?
						ServerResponse.noContent().build() :
						ServerResponse.ok().contentType(json).bodyValue(list));
	}

	public Mono<ServerResponse> createPerson(ServerRequest request) {
		final String principalName = "User"; //This one we should get from security context
		return request.bodyToMono(Person.class)
				.flatMap(newPers -> personServer.createNewPerson(newPers, principalName))
				.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data))
				.onErrorResume(InvalidPersonException.class, 
						ex -> ServerResponse.badRequest().contentType(json).body(
								Mono.just(ExceptionUtils.listValidationErrors(ex.getViolations())), ErrorInfo.class));
	}

	public Mono<ServerResponse> updatePerson(ServerRequest request) {
		final String principalName = "User"; //This one we should get from security context
		return request
				.bodyToMono(Person.class)
				.flatMap(updatedPerson -> personServer.updatePerson(updatedPerson, principalName))
				.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data))
				.switchIfEmpty(ServerResponse.notFound().build())
				.onErrorResume(InvalidPersonException.class, 
						ex -> ServerResponse.badRequest().contentType(json).body(
								Mono.just(ExceptionUtils.listValidationErrors(ex.getViolations())), ErrorInfo.class));
	}

	public Mono<ServerResponse> deletePerson(ServerRequest request) {
		String idParam = request.pathVariable("id");
		try {
			UUID id = UUID.fromString(idParam);
			return personServer.deletePerson(id)
					.flatMap(data -> ServerResponse.ok().build())
					.switchIfEmpty(ServerResponse.notFound().build());
		} catch (IllegalArgumentException iae) {
			Mono<ErrorInfo> errorInfo = Mono.just(new ErrorInfo("UUID", "Illegal UUID value " + idParam, LocalDateTime.now()));
			return ServerResponse.badRequest().contentType(json).body(errorInfo, ErrorInfo.class);
		}
	}
}
