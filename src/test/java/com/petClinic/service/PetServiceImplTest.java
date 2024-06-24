//package com.petClinic.service;
//
//import com.petClinic.domain.Pet;
//import com.petClinic.mapper.PetMapper;
//import com.petClinic.mapper.PetMapperImpl;
//import com.petClinic.model.PetDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import reactor.core.publisher.Mono;
//
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;
//@SpringBootTest
//class PetServiceImplTest {
//
//    @Autowired
//    PetService petService;
//
//    @Autowired
//    PetMapper petMapper;
//
//    PetDTO petDTO;
//
//    @BeforeEach
//    void setUp() {
//        petDTO = petMapper.toDTO(createTestPet());
//    }
//
//    public static Pet createTestPet() {
//        return Pet.builder()
//                .name("Test Pet")
//                .age(3)
//                .petType("Owl")
//                .build();
//    }
//
//    public static PetDTO getTestPetDTO(){
//        return new PetMapperImpl().toDTO(createTestPet());
//    }
//
//    public PetDTO getSavedPetDTO(){
//        return petService.savePetMono(Mono.just(getTestPetDTO())).block();
//    }
//
//    //~~~~~~~~~~~~~~~~~~~~~~~
//    @Test
//    @DisplayName("Test save pet using Subscriber")
//    void testSavePet() {
//        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
//        AtomicReference<PetDTO> atomicDTO = new AtomicReference<>();
//
//        Mono<PetDTO> savedMono = petService.savePetMono(Mono.just(petDTO));
//
//        savedMono.subscribe(savedDTO -> {
//            System.out.println(savedDTO.getId());
//            atomicBoolean.set(true);
//            atomicDTO.set(savedDTO);
//        });
//
//        await().untilTrue(atomicBoolean);
//
//        PetDTO persistedDTO = atomicDTO.get();
//        assertThat(persistedDTO).isNotNull();
//        assertThat(persistedDTO.getId()).isNotNull();
//    }
//
//    @Test
//    @DisplayName("Test save pet using block")
//    void testSavePetBlock() {
//        PetDTO savedDto = petService.savePetMono(Mono.just(getTestPetDTO())).block();
//
//        assertThat(savedDto).isNotNull();
//        assertThat(savedDto.getId()).isNotNull();
//    }
//
//    @Test
//    @DisplayName("Test update pet using block")
//    void updatePetBlock() {
//        final String newName = "New Pet Name";
//        PetDTO savedPetDTO = getSavedPetDTO();
//        savedPetDTO.setName(newName);
//
//        PetDTO updatedDTO = petService.savePetMono(Mono.just(savedPetDTO)).block();
//
//        //verify exists in db
//        PetDTO fetchedDTO = petService.getById(updatedDTO.getId()).block();
//        assertThat(fetchedDTO.getName()).isEqualTo(newName);
//    }
//
//    @Test
//    @DisplayName("Test update pet using reactive streams")
//    void testUpdatePet() {
//       final String newName = "New Pet Name";
//       AtomicReference<PetDTO> atomicDTO = new AtomicReference<>();
//
//       petService.savePetMono(Mono.just(getTestPetDTO()))
//               .map(savedPetDTO -> {
//                   savedPetDTO.setName(newName);
//                   return savedPetDTO;
//               })
//               .flatMap(petService::savePet)
//               .flatMap(savedUpdatedDTO -> petService.getById(savedUpdatedDTO.getId()))  //get from DB
//               .subscribe(dtoFromDB -> atomicDTO.set(dtoFromDB));
//            await().until(() -> atomicDTO.get() != null);
//            assertThat(atomicDTO.get().getName()).isEqualTo(newName);
//    }
//
//    @Test
//    void deletePet() {
//        PetDTO petDtoToDelete = getSavedPetDTO();
//        petService.deletePetById(petDtoToDelete.getId()).block();
//        Mono<PetDTO> expectedEmptyPetMono = petService.getById(petDtoToDelete.getId());
//        PetDTO emptyPet = expectedEmptyPetMono.block();
//
//        assertThat(emptyPet).isNull();
//    }
//
//    @Test
//    void testFindFirstByName() {
//       AtomicBoolean atomicBoolean = new AtomicBoolean();
//       PetDTO petDTO = getSavedPetDTO();
//       Mono<PetDTO> foundDTO = petService.findFirstByPetName(petDTO.getName());
//
//       foundDTO.subscribe(dto -> {
//           System.out.println(dto.toString());
//           atomicBoolean.set(true);
//       });
//       await().untilTrue(atomicBoolean);
//    }
//
//    @Test
//    void testFindByPetType() {
//        PetDTO petDTO = getSavedPetDTO();
//        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
//
//        petService.findByPetType(petDTO.getPetType())
//                .subscribe(dto -> {
//                    System.out.println(dto.toString());
//                    atomicBoolean.set(true);
//                });
//        await().untilTrue(atomicBoolean);
//    }
//
//}