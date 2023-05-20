package com.petClinic.startup;

import com.petClinic.domain.Owner;
import com.petClinic.domain.Pet;
import com.petClinic.repository.OwnerRepository;
import com.petClinic.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class Startup implements CommandLineRunner {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadPetData();
        loadOwnerData();
    }

    private void loadPetData() {
        petRepository.count().subscribe(count -> {
            if (count == 0) {
                Pet pet1 = Pet.builder()
                        .name("Rocco")
                        .petType("Cat")
                        .age(2)
                        .birthdate(LocalDate.now())
                        .build();

                Pet pet2 = Pet.builder()
                        .name("Yoda")
                        .petType("Bird")
                        .age(5)
                        .birthdate(LocalDate.now())
                        .build();

                Pet pet3 = Pet.builder()
                        .name("Nyx")
                        .petType("Dog")
                        .age(3)
                        .birthdate(LocalDate.now())
                        .build();

                petRepository.save(pet1).subscribe();
                petRepository.save(pet2).subscribe();
                petRepository.save(pet3).subscribe();
            }
        });

    }

    private void loadOwnerData() {

        ownerRepository.count().subscribe(count -> {
            if (count == 0) {
                Owner owner1 = Owner.builder()
                        .name("Owner 1")
                        .address("Grasse")
                        .telephone("1234")
                        .build();

                Owner owner2 = Owner.builder()
                        .name("Owner 2")
                        .address("Lisbon")
                        .telephone("4321")
                        .build();

                Owner owner3 = Owner.builder()
                        .name("Owner 3")
                        .address("Amboise")
                        .telephone("0123")
                        .build();

                ownerRepository.save(owner1).subscribe();
                ownerRepository.save(owner2).subscribe();
                ownerRepository.save(owner3).subscribe();
            }
        });
    }
}