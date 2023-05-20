package com.petClinic.repository;

import com.petClinic.domain.Pet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetRepository extends ReactiveMongoRepository<Pet, String> {

    Mono<Pet> findFirstByName(String name);

    Flux<Pet> findByPetType(String type);

}