package com.petClinic.startup;

import com.petClinic.domain.Owner;
import com.petClinic.domain.Pet;
import com.petClinic.repository.OwnerRepository;
import com.petClinic.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class Startup implements CommandLineRunner {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    @Override
    public void run(String... args) {
        loadInitialData()
                .subscribe();
    }

    private Mono<Void> loadInitialData() {
        return petRepository.deleteAll()
                .thenMany(loadPetData())
                .thenMany(ownerRepository.deleteAll())
                .thenMany(loadOwnerData())
                .then();
    }

    private Flux<Pet> loadPetData() {
        return petRepository.count()
                .filter(count -> count == 0)
                .flatMapMany(count -> {
                    Pet rocco = Pet.builder()
                            .name("Rocco")
                            .petType("Cat")
                            .age(2)
                            .birthdate(LocalDate.now())
                            .build();

                    Pet yoda = Pet.builder()
                            .name("Yoda")
                            .petType("Bird")
                            .age(5)
                            .birthdate(LocalDate.now())
                            .build();

                    Pet nyx = Pet.builder()
                            .name("Nyx")
                            .petType("Dog")
                            .age(3)
                            .birthdate(LocalDate.now())
                            .build();

                    return petRepository.saveAll(Flux.just(rocco, yoda, nyx));
                });
    }


    private Flux<Owner> loadOwnerData() {
        return ownerRepository.count()
                .filter(count -> count == 0)
                .flatMapMany(count -> {
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

                    return ownerRepository.saveAll(Flux.just(owner1, owner2, owner3));
                });
    }
}