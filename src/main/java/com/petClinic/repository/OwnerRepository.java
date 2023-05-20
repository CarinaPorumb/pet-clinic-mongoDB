package com.petClinic.repository;

import com.petClinic.domain.Owner;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OwnerRepository extends ReactiveMongoRepository<Owner, String> {

    Mono<Owner> findFirstByName(String name);

    Flux<Owner> findByAddress(String address);

}