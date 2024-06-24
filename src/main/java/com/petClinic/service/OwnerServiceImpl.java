package com.petClinic.service;

import com.petClinic.exception.NotFoundException;
import com.petClinic.mapper.OwnerMapper;
import com.petClinic.model.OwnerDTO;
import com.petClinic.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    public Mono<OwnerDTO> getById(String id) {
        return ownerRepository.findById(id)
                .map(ownerMapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Owner not found with id: " + id)));
    }

    @Override
    public Flux<OwnerDTO> listOwners() {
        return ownerRepository.findAll().map(ownerMapper::toDTO);
    }

    @Override
    public Mono<OwnerDTO> createOwnerMono(Mono<OwnerDTO> dtoMono) {
        return dtoMono.map(ownerMapper::toEntity)
                .flatMap(ownerRepository::save)
                .map(ownerMapper::toDTO);
    }

    @Override
    public Mono<OwnerDTO> createOwner(OwnerDTO dto) {
        return ownerRepository.save(ownerMapper.toEntity(dto))
                .map(ownerMapper::toDTO);
    }

    @Override
    public Mono<OwnerDTO> updateOwner(String id, OwnerDTO dto) {
        return ownerRepository.findById(id)
                .flatMap(foundOwner -> {
                    foundOwner.setName(dto.getName());
                    foundOwner.setAddress(dto.getAddress());
                    foundOwner.setTelephone(dto.getTelephone());
                    return ownerRepository.save(foundOwner);
                })
                .map(ownerMapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Owner not found with id: " + id)));
    }

    @Override
    public Mono<Void> deleteOwnerById(String id) {
        return ownerRepository.deleteById(id);
    }

    @Override
    public Mono<OwnerDTO> patchOwner(String id, OwnerDTO dto) {
        return ownerRepository.findById(id)
                .flatMap(foundOwner -> {
                    if (StringUtils.hasText(dto.getName())) {
                        foundOwner.setName(dto.getName());
                    }
                    if (StringUtils.hasText(dto.getAddress())) {
                        foundOwner.setAddress(dto.getAddress());
                    }
                    if (StringUtils.hasText(dto.getTelephone())) {
                        foundOwner.setTelephone(dto.getTelephone());
                    }
                    return ownerRepository.save(foundOwner);
                })
                .map(ownerMapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Owner not found with id: " + id)));
    }

    @Override
    public Mono<OwnerDTO> findFirstByName(String name) {
        return ownerRepository.findFirstByName(name)
                .map(ownerMapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Owner not found with name: " + name)));
    }

    @Override
    public Flux<OwnerDTO> findByAddress(String address) {
        return ownerRepository.findByAddress(address).map(ownerMapper::toDTO);
    }

}