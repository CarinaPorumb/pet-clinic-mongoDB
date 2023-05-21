package com.petClinic.webFN;

import com.petClinic.model.OwnerDTO;
import com.petClinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static com.petClinic.webFN.OwnerRouterConfig.OWNER_PATH_ID;

@RequiredArgsConstructor
@Component
public class OwnerHandler {

    private final OwnerService ownerService;
    private final Validator validator;

    private void validate(OwnerDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "ownerDTO");
        validator.validate(dto, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> listOwners(ServerRequest request) {
        return ServerResponse.ok().body(ownerService.listOwners(), OwnerDTO.class);
    }

    public Mono<ServerResponse> getOwnerById(ServerRequest request) {
        return ServerResponse.ok()
                .body(ownerService.getById(request.pathVariable("id"))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))), OwnerDTO.class);


    }

    public Mono<ServerResponse> createOwner(ServerRequest request) {
        return ownerService.saveOwnerMono(request.bodyToMono(OwnerDTO.class).doOnNext(this::validate))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(ownerDTO -> ServerResponse.created(UriComponentsBuilder
                                .fromPath(OWNER_PATH_ID)
                                .build(ownerDTO.getId()))
                        .build());

    }

    public Mono<ServerResponse> updateOwner(ServerRequest request) {
        return request.bodyToMono(OwnerDTO.class).doOnNext(this::validate)
                .flatMap(ownerDTO -> ownerService.updateOwner(request.pathVariable("id"), ownerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedOwner -> ServerResponse.noContent().build());
    }


    public Mono<ServerResponse> deleteOwner(ServerRequest request) {
        return ownerService.getById(request.pathVariable("id"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(ownerDTO -> ownerService.deleteOwnerById(ownerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchOwner(ServerRequest request) {
        return request.bodyToMono(OwnerDTO.class).doOnNext(this::validate)
                .flatMap(ownerDTO -> ownerService.patchOwner(request.pathVariable("id"), ownerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedOwner -> ServerResponse.noContent().build());
    }

}