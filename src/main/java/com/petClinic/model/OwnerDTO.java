package com.petClinic.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO {

    private String id;

    @NotBlank
    private String name;

    private String address;

    private String telephone;

}