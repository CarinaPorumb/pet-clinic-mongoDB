package com.petClinic.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {

    private String id;

    @NotBlank
    private String name;

    private String petType;

    private Integer age;

    private LocalDate birthdate;

}