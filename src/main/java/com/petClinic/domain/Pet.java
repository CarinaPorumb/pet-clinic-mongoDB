package com.petClinic.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "pets")
public class Pet {

    @Id
    private String id;

    @NotBlank
    private String name;

    private String petType;

    private Integer age;

    private LocalDate birthdate;

}