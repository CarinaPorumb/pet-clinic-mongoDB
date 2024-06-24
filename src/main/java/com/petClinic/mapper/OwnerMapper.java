package com.petClinic.mapper;

import com.petClinic.domain.Owner;
import com.petClinic.model.OwnerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    OwnerDTO toDTO(Owner owner);

    Owner toEntity(OwnerDTO dto);

}