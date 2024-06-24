package com.petClinic.service;

import com.petClinic.model.PetDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetService {

    Mono<PetDTO> getById(String id);

    Flux<PetDTO> listPets();

    Mono<PetDTO> createPetMono(Mono<PetDTO> dtoMono);

    Mono<PetDTO> createPet(PetDTO dto);

    Mono<PetDTO> updatePet(String id, PetDTO dto);

    Mono<Void> deletePetById(String id);

    Mono<PetDTO> patchPet(String id, PetDTO dto);

    Mono<PetDTO> findFirstByPetName(String name);

    Flux<PetDTO> findByPetType(String type);

}