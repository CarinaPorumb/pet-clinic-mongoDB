package com.petClinic.mapper;

import com.petClinic.domain.Owner;
import com.petClinic.model.OwnerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface OwnerMapper {

    OwnerDTO ownerToOwnerDTO(Owner owner);

    Owner ownerDTOToOwner(OwnerDTO dto);

}