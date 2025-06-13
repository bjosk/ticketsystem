package com.ticketsystem.ticketsystem.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void getTicketsByUserIdEndpoint() throws Exception {
        mockMvc.perform(get("/api/users/" + 15 + "/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ticketId").value(94));
    }

    @Test
    void searchAgentAndAdminUsersEndpoint() throws Exception {
        String query = "ano";
        mockMvc.perform(get("/api/users/searchAgentAndAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .param("usernameQuery", query)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("AnotherTest"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }

    @Test
    void searchAllUsersEndpoint() throws Exception {
        String query = "";
        mockMvc.perform(get("/api/users/searchAllUsers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("usernameQuery", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    void updateUserEndpoint() throws Exception {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("agent", "agentNew", "agentNew@example.com", "USER");
        mockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userUpdateRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<User> updatedUser = userRepository.findByUsername("agentNew");
        assertEquals(userUpdateRequest.newUsername(), updatedUser.orElseThrow().getUsername());
        assertEquals(userUpdateRequest.email(), updatedUser.orElseThrow().getEmail());
        assertEquals(Role.valueOf(userUpdateRequest.role()), updatedUser.orElseThrow().getRole());

    }

    @Test
    void createUserEndpoint() throws Exception {
        UserRequest userRequest = new UserRequest("fakeUser22", passwordEncoder.encode("something"), "fake@example.com");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(userRepository.findByUsername("fakeUser22").isPresent());
    }

    @Test
    void getAllUsersEndpoint() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
