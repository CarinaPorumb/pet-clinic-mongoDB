package com.petClinic.webFN;

import com.petClinic.domain.Owner;
import com.petClinic.model.OwnerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.petClinic.webFN.OwnerRouterConfig.OWNER_PATH;
import static com.petClinic.webFN.OwnerRouterConfig.OWNER_PATH_ID;
import static org.hamcrest.Matchers.greaterThan;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class OwnerHandlerTest {

    @Autowired
    WebTestClient webTestClient;


    @Test
    @Order(1)
    void testListOwners() {
        webTestClient.get().uri(OWNER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }

    @Test
    @Order(2)
    void testGetOwnerById() {
        OwnerDTO ownerDTO = getSavedTestOwner();

        webTestClient.get().uri(OWNER_PATH_ID, ownerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(OwnerDTO.class);
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(OWNER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateOwner() {

        webTestClient.post().uri((OWNER_PATH))
                .body(Mono.just(getTestOwner()), OwnerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testCreateOwnerBadData() {
        Owner testOwner = getTestOwner();
        testOwner.setName("");

        webTestClient.post().uri(OWNER_PATH)
                .body(Mono.just(testOwner), OwnerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateOwner() {
        OwnerDTO ownerDTO = getSavedTestOwner();
        webTestClient.put().uri(OWNER_PATH_ID, ownerDTO.getId())
                .body(Mono.just(ownerDTO), OwnerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateOwnerNotFound() {
        webTestClient.put().uri(OWNER_PATH_ID, 999)
                .body(Mono.just(getTestOwner()), OwnerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateOwnerBadRequest() {
        OwnerDTO testOwner = getSavedTestOwner();
        testOwner.setName("");

        webTestClient.put().uri(OWNER_PATH_ID, OwnerDTO.class)
                .body(Mono.just(testOwner), OwnerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(99)
    void testDeleteOwner() {
        OwnerDTO ownerDTO = getSavedTestOwner();

        webTestClient.delete().uri(OWNER_PATH_ID, ownerDTO.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteOwnerIdNotFound() {
        webTestClient.delete().uri(OWNER_PATH_ID, 9999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testPatchOwner() {
        OwnerDTO ownerDTO = getSavedTestOwner();

        webTestClient.patch().uri(OWNER_PATH_ID, ownerDTO.getId())
                .body(Mono.just(ownerDTO), OwnerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchOwnerIdNotFound() {
        webTestClient.patch().uri(OWNER_PATH_ID, 999)
                .body(Mono.just(getTestOwner()), OwnerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }


    public static Owner getTestOwner() {
        return Owner.builder()
                .name("Test Owner")
                .build();
    }

    public OwnerDTO getSavedTestOwner() {
        FluxExchangeResult<OwnerDTO> ownerDTOFluxExchangeResult = webTestClient.post().uri(OWNER_PATH)
                .body(Mono.just(getTestOwner()), OwnerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(OwnerDTO.class);

        List<String> location = ownerDTOFluxExchangeResult.getRequestHeaders().get("Location");
        return webTestClient.get().uri(OWNER_PATH)
                .exchange()
                .returnResult(OwnerDTO.class)
                .getResponseBody().blockFirst();
    }
}