package com.petClinic.service;

import com.petClinic.model.OwnerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OwnerService {

    Mono<OwnerDTO> getById(String id);

    Flux<OwnerDTO> listOwners();

    Mono<OwnerDTO> saveOwnerMono(Mono<OwnerDTO> dtoMono);

    Mono<OwnerDTO> saveOwner(OwnerDTO dto);

    Mono<OwnerDTO> updateOwner(String id, OwnerDTO dto);

    Mono<Void> deleteOwnerById(String id);

    Mono<OwnerDTO> patchOwner(String id, OwnerDTO dto);

    Mono<OwnerDTO> findFirstByName(String name);

    Flux<OwnerDTO> findByAddress(String address);

}