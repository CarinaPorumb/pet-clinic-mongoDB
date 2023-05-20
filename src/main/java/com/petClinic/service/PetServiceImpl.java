package com.petClinic.service;

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
                .map(petMapper::petToPetDTO);
    }

    @Override
    public Flux<PetDTO> listPets() {
        return petRepository.findAll().map(petMapper::petToPetDTO);
    }

    @Override
    public Mono<PetDTO> savePetMono(Mono<PetDTO> dtoMono) {
        return dtoMono.map(petMapper::petDTOToPet)
                .flatMap(petRepository::save)
                .map(petMapper::petToPetDTO);
    }

    @Override
    public Mono<PetDTO> savePet(PetDTO dto) {
        return petRepository.save(petMapper.petDTOToPet(dto))
                .map(petMapper::petToPetDTO);
    }

    @Override
    public Mono<PetDTO> updatePet(String id, PetDTO dto) {
        return petRepository.findById(id)
                .map(foundPet -> {
                    foundPet.setName(dto.getName());
                    foundPet.setPetType(dto.getPetType());
                    foundPet.setAge(dto.getAge());
                    foundPet.setBirthdate(dto.getBirthdate());
                    return foundPet;
                }).flatMap(petRepository::save)
                .map(petMapper::petToPetDTO);
    }

    @Override
    public Mono<Void> deletePetById(String id) {
        return petRepository.deleteById(id);
    }

    @Override
    public Mono<PetDTO> patchPet(String id, PetDTO dto) {
        return petRepository.findById(id)
                .map(foundPet -> {
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
                    return foundPet;
                }).flatMap(petRepository::save)
                .map(petMapper::petToPetDTO);
    }

    @Override
    public Mono<PetDTO> findFirstByPetName(String name) {
        return petRepository.findFirstByName(name)
                .map(petMapper::petToPetDTO);
    }

    @Override
    public Flux<PetDTO> findByPetType(String type) {
        return petRepository.findByPetType(type)
                .map(petMapper::petToPetDTO);
    }
}
