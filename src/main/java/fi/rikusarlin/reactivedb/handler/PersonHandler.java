package fi.rikusarlin.reactivedb.handler;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import fi.rikusarlin.reactivedb.exception.ErrorInfo;
import fi.rikusarlin.reactivedb.exception.ExceptionUtils;
import fi.rikusarlin.reactivedb.exception.InvalidPersonException;
import fi.rikusarlin.reactivedb.model.Person;
import fi.rikusarlin.reactivedb.service.PersonServer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PersonHandler {

	private MediaType json = MediaType.APPLICATION_JSON;

	private final PersonServer personServer;

	public Mono<ServerResponse> getAll(ServerRequest request) {
		return personServer.findAllPersons().collectList()
				.flatMap(list -> list.isEmpty() ?
						ServerResponse.noContent().build() :
							ServerResponse.ok().contentType(json).body(list, Person.class))
				.onErrorResume(error -> ServerResponse.badRequest().build());
	}

	public Mono<ServerResponse> getById(ServerRequest request) {
		String idParam = request.pathVariable("id");
		try {
			return personServer
					.findById(UUID.fromString(idParam))
					.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data))
					.switchIfEmpty(ServerResponse.notFound().build())
					.onErrorResume(error -> ServerResponse.badRequest().build());
		} catch (IllegalArgumentException iae) {
			Mono<ErrorInfo> errorInfo = Mono.just(
					ErrorInfo.builder().field("UUID").message("Illegal UUID value " + idParam).timestamp(LocalDateTime.now()).build());
			return ServerResponse.badRequest().contentType(json).body(errorInfo, ErrorInfo.class);
		}
	}

	public Mono<ServerResponse> getByLastName(ServerRequest request) {
		return personServer.findByLastName(request.pathVariable("lastName")).collectList()
				.flatMap(list -> list.isEmpty() ?
						ServerResponse.noContent().build() :
						ServerResponse.ok().contentType(json).body(list, Person.class))
				.onErrorResume(error -> ServerResponse.badRequest().build());
	}

	public Mono<ServerResponse> createPerson(ServerRequest request) {
		final String principalName = (request.principal().block() != null) ? request.principal().block().getName() : "" ;
		return request.bodyToMono(Person.class)
				.flatMap(newPers -> personServer.createNewPerson(newPers, principalName))
				.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data))
				.onErrorResume(InvalidPersonException.class, 
						ex -> ServerResponse.badRequest().contentType(json).body(
								Mono.just(ExceptionUtils.listValidationErrors(ex.getViolations())), ErrorInfo.class))
				.onErrorResume(error -> ServerResponse.badRequest().build());
	}

	public Mono<ServerResponse> updatePerson(ServerRequest request) {
		final String principalName = (request.principal().block() != null) ? request.principal().block().getName() : "" ;
		return request
				.bodyToMono(Person.class)
				.flatMap(updatedPerson -> personServer.updatePerson(updatedPerson, principalName))
				.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data))
				.switchIfEmpty(ServerResponse.notFound().build())
				.onErrorResume(InvalidPersonException.class, 
						ex -> ServerResponse.badRequest().contentType(json).body(
								Mono.just(ExceptionUtils.listValidationErrors(ex.getViolations())), ErrorInfo.class))
				.onErrorResume(error -> ServerResponse.badRequest().build());
	}

	public Mono<ServerResponse> deletePerson(ServerRequest request) {
		String idParam = request.pathVariable("id");
		try {
			return personServer.deletePerson(UUID.fromString(idParam))
					.flatMap(data -> ServerResponse.ok().contentType(json).bodyValue(data))
					.switchIfEmpty(ServerResponse.notFound().build())
					.onErrorResume(error -> ServerResponse.badRequest().build());
		} catch (IllegalArgumentException iae) {
			Mono<ErrorInfo> errorInfo = Mono.just(
					ErrorInfo.builder().field("UUID").message("Illegal UUID value " + idParam).timestamp(LocalDateTime.now()).build());
			return ServerResponse.badRequest().contentType(json).body(errorInfo, ErrorInfo.class);
		}
	}
}
