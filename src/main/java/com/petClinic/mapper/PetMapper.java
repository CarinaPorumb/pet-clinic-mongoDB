package com.petClinic.mapper;

import com.petClinic.domain.Pet;
import com.petClinic.model.PetDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PetMapper {

    PetDTO petToPetDTO(Pet pet);

    Pet petDTOToPet(PetDTO dto);

}