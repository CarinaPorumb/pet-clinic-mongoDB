package com.petClinic.webFN;

import com.petClinic.model.PetDTO;
import com.petClinic.service.PetService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.petClinic.webFN.PetRouterConfig.PET_PATH_ID;

@RequiredArgsConstructor
@Component
public class PetHandler {

    private final PetService petService;
    private final Validator validator;

    private void validate(PetDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "petDTO");
        validator.validate(dto, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> listPets(ServerRequest request) {
        Flux<PetDTO> flux;

        if (request.queryParam("petType").isPresent()) {
            flux = petService.findByPetType(request.queryParam("petType").get());
        } else {
            flux = petService.listPets();
        }
        return ServerResponse.ok().body(flux, PetDTO.class);
    }

    public Mono<ServerResponse> getPetById(ServerRequest request) {
        return ServerResponse.ok()
                .body(petService.getById(request.pathVariable("id"))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))), PetDTO.class);
    }

    public Mono<ServerResponse> createPet(ServerRequest request) {
        return petService.savePetMono(request.bodyToMono(PetDTO.class).doOnNext(this::validate))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(petDTO -> ServerResponse.created(UriComponentsBuilder
                                .fromPath(PET_PATH_ID)
                                .build(petDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> updatePet(ServerRequest request) {
        return request.bodyToMono(PetDTO.class)
                .doOnNext(this::validate)
                .flatMap(petDTO -> petService.updatePet(request.pathVariable("id"), petDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDTO -> ServerResponse.noContent().build());

    }

    public Mono<ServerResponse> deletePet(ServerRequest request) {
        return petService.getById(request.pathVariable("id"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(petDTO -> petService.deletePetById(petDTO.getId()))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchPet(ServerRequest request) {
        return request.bodyToMono(PetDTO.class)
                .doOnNext(this::validate)
                .flatMap(petDTO -> petService.patchPet(request.pathVariable("id"), petDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDTO -> ServerResponse.noContent().build());
    }

}