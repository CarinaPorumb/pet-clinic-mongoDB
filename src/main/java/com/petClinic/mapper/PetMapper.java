package com.petClinic.mapper;

import com.petClinic.domain.Pet;
import com.petClinic.model.PetDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {

    PetDTO toDTO(Pet pet);

    Pet toEntity(PetDTO dto);

}