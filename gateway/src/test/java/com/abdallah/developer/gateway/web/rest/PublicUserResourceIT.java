package com.abdallah.developer.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.abdallah.developer.gateway.IntegrationTest;
import com.abdallah.developer.gateway.config.Constants;
import com.abdallah.developer.gateway.config.TestSecurityConfiguration;
import com.abdallah.developer.gateway.domain.User;
import com.abdallah.developer.gateway.repository.EntityManager;
import com.abdallah.developer.gateway.repository.UserRepository;
import com.abdallah.developer.gateway.security.AuthoritiesConstants;
import com.abdallah.developer.gateway.service.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_TIMEOUT)
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class PublicUserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private User user;

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        user = UserResourceIT.initTestUser(userRepository, em);
    }

    @Test
    void getAllPublicUsers() {
        // Initialize the database
        userRepository.create(user).block();

        // Get all the users
        UserDTO foundUser = webTestClient
            .get()
            .uri("/api/users?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .returnResult(UserDTO.class)
            .getResponseBody()
            .blockFirst();

        assertThat(foundUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
    }

    @Test
    void getAllAuthorities() {
        webTestClient
            .get()
            .uri("/api/authorities")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$[?(@=='" + AuthoritiesConstants.ADMIN + "')]")
            .hasJsonPath()
            .jsonPath("$[?(@=='" + AuthoritiesConstants.USER + "')]")
            .hasJsonPath();
    }
}
