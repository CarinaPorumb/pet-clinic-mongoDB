package com.petClinic.service;

import com.petClinic.exception.NotFoundException;
import com.petClinic.mapper.PetMapper;
import com.petClinic.model.PetDTO;
import com.petClinic.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;


    @Override
    public Mono<PetDTO> getById(String id) {
        return petRepository.findById(id)
                .map(petMapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Pet not found with id " + id)));
    }

    @Override
    public Flux<PetDTO> listPets() {
        return petRepository.findAll().map(petMapper::toDTO);
    }

    @Override
    public Mono<PetDTO> createPetMono(Mono<PetDTO> dtoMono) {
        return dtoMono.map(petMapper::toEntity)
                .flatMap(petRepository::save)
                .map(petMapper::toDTO);
    }

    @Override
    public Mono<PetDTO> createPet(PetDTO dto) {
        return petRepository.save(petMapper.toEntity(dto))
                .map(petMapper::toDTO);
    }

    @Override
    public Mono<PetDTO> updatePet(String id, PetDTO dto) {
        return petRepository.findById(id)
                .flatMap(foundPet -> {
                    foundPet.setName(dto.getName());
                    foundPet.setPetType(dto.getPetType());
                    foundPet.setAge(dto.getAge());
                    foundPet.setBirthdate(dto.getBirthdate());
                    return petRepository.save(foundPet);
                })
                .map(petMapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Pet not found with id " + id)));
    }

    @Override
    public Mono<Void> deletePetById(String id) {
        return petRepository.deleteById(id);
    }

    @Override
    public Mono<PetDTO> patchPet(String id, PetDTO dto) {
        return petRepository.findById(id)
                .flatMap(foundPet -> {
                    if (StringUtils.hasText(dto.getName())) {
                        foundPet.setName(dto.getName());
                    }
                    if (StringUtils.hasText(dto.getPetType())) {
                        foundPet.setPetType(dto.getPetType());
                    }
                    if (dto.getAge() != null) {
                        foundPet.setAge(dto.getAge());
                    }
                    if (dto.getBirthdate() != null) {
                        foundPet.setBirthdate(dto.getBirthdate());
                    }
                    return petRepository.save(foundPet);
                })
                .map(petMapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Pet not found with id " + id)));
    }

    @Override
    public Mono<PetDTO> findFirstByPetName(String name) {
        return petRepository.findFirstByName(name)
                .map(petMapper::toDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("Pet not found with name " + name)));
    }

    @Override
    public Flux<PetDTO> findByPetType(String type) {
        return petRepository.findByPetType(type)
                .map(petMapper::toDTO);
    }

}