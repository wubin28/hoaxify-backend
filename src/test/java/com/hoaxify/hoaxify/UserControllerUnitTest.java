package com.hoaxify.hoaxify;

import com.hoaxify.hoaxify.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Test
	void postUser_whenUserIsValid_userSavedToDatabase() throws Exception {
		String newUserInJson = """
                        {
                        "username": "ben",
                        "displayName": "Ben",
                        "password": "P4ssword"
                        }
				""";
		mockMvc.perform(MockMvcRequestBuilders.post("/api/1.0/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newUserInJson))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("user saved"));
		assertThat(userRepository.findByUsername("ben").get(0).getDisplayName()).isEqualTo("Ben");
	}

}
