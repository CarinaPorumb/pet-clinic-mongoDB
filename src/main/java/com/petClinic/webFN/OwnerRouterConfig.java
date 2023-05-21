package com.petClinic.webFN;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RequiredArgsConstructor
@Configuration
public class OwnerRouterConfig {

    public static final String OWNER_PATH = "/api/v3/owner";
    public static final String OWNER_PATH_ID = OWNER_PATH + "/{id}";

    private final OwnerHandler handler;

    @Bean
    public RouterFunction<ServerResponse> ownerRoutes() {
        return route()
                .GET(OWNER_PATH_ID, accept(APPLICATION_JSON), handler::getOwnerById)
                .GET(OWNER_PATH, accept(APPLICATION_JSON), handler::listOwners)
                .POST(OWNER_PATH, accept(APPLICATION_JSON), handler::createOwner)
                .PUT(OWNER_PATH_ID, accept(APPLICATION_JSON), handler::updateOwner)
                .DELETE(OWNER_PATH_ID, accept(APPLICATION_JSON), handler::deleteOwner)
                .PATCH(OWNER_PATH_ID, accept(APPLICATION_JSON), handler::patchOwner)
                .build();
    }

}