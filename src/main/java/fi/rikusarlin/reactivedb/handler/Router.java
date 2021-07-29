package fi.rikusarlin.reactivedb.handler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Component
public class Router {

    private final MediaType json = MediaType.APPLICATION_JSON;

    @Bean
    public RouterFunction<ServerResponse> personRoutes(PersonHandler personHandler){
        return RouterFunctions
                .route(GET("/persons/{id}").and(accept(json)), personHandler::getById)
                .andRoute(GET("/persons").and(accept(json)), personHandler::getAll)
                .andRoute(GET("/persons/byLastName/{lastName}").and(accept(json)), personHandler::getByLastName)
                .andRoute(POST("/persons").and(accept(json)), personHandler::createPerson)
                .andRoute(PUT("/persons").and(accept(json)), personHandler::updatePerson)
                .andRoute(DELETE("/persons/{id}").and(accept(json)), personHandler::deletePerson)
                ;
    }
    

}
