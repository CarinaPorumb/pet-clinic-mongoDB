package com.petClinic.webFN;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class PetRouterConfig {

    public static final String PET_PATH = "/api/v3/pet";
    public static final String PET_PATH_ID = PET_PATH + "/{id}";

    private final PetHandler handler;

    @Bean
    public RouterFunction<ServerResponse> petRoutes() {
        return route()
                .GET(PET_PATH, accept(APPLICATION_JSON), handler::listPets)
                .GET(PET_PATH_ID, accept(APPLICATION_JSON), handler::getPetById)
                .POST(PET_PATH, accept(APPLICATION_JSON), handler::createPet)
                .PUT(PET_PATH_ID, accept(APPLICATION_JSON), handler::updatePet)
                .DELETE(PET_PATH_ID, accept(APPLICATION_JSON), handler::deletePet)
                .PATCH(PET_PATH_ID, accept(APPLICATION_JSON), handler::patchPet)
                .build();
    }

}