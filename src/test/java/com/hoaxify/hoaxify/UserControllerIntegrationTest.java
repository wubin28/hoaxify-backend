package com.hoaxify.hoaxify;

import com.hoaxify.hoaxify.user.User;
import com.hoaxify.hoaxify.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void postUser_whenUserIsValid_userSavedToDatabase() {
        this.webTestClient
                .post()
                .uri("/api/1.0/users")
                .bodyValue("""
                        {
                        "username": "ben",
                        "displayName": "Ben",
                        "password": "P4ssword"
                        }
                        """)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("user saved");
        assertThat(userRepository.findByUsername("ben").get(0).getDisplayName()).isEqualTo("Ben");
    }

    @Test
    void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
        this.webTestClient
                .post()
                .uri("/api/1.0/users")
                .bodyValue("""
                        {
                        "username": "ben",
                        "displayName": "Ben",
                        "password": "P4ssword"
                        }
                        """)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("user saved");
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        assertThat(user.getPassword()).isNotEqualTo("P4ssword");
    }
}
