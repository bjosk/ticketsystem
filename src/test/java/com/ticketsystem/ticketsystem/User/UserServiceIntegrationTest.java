package com.ticketsystem.ticketsystem.User;

import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketRepository;
import com.ticketsystem.ticketsystem.Ticket.TicketResponse;
import com.ticketsystem.ticketsystem.Ticket.TicketStatus;
import jakarta.transaction.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@AutoConfigureMockMvc
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Should successfully create a new user and persist it.
     */
    @Test
    void createUser_success() throws Exception {
        UserRequest userRequest1 = new UserRequest("JohnDoe", "12345", "johndoe@example.com");
        UserResponse userResponse1 = userService.createUser(userRequest1);

        assertEquals(userResponse1.username(), userRequest1.username());
        assertTrue(userRepository.findByUsername("JohnDoe").isPresent());

    }

    /**
     * Should throw conflict exception when creating a user with an existing username.
     */
    @Test
    void createUser_fail() throws Exception {
        UserRequest userRequest1 = new UserRequest("AnotherTest", "12345", "johndoe@example.com");
        assertThrows(ResponseStatusException.class, () -> userService.createUser(userRequest1));
    }

    /**
     * Should update an existing user.
     */
    @Test
    void updateUser_success() {
        UserUpdateRequest upd = new UserUpdateRequest("AnotherTest", "updatedTest", "new@example.com", "ADMIN");
        userService.updateUser(upd);
        Optional<User> updatedUser = userRepository.findByUsername("updatedTest");
        assertEquals(upd.newUsername(), userRepository.findByUsername("updatedTest").orElseThrow().getUsername());
        assertEquals(upd.email(), userRepository.findByUsername("updatedTest").orElseThrow().getEmail());
        assertEquals(Role.valueOf(upd.role()), userRepository.findByUsername("updatedTest").orElseThrow().getRole());
    }

    /**
     * Should throw not found exception when updating a non-existent user.
     */
    @Test
    void updateUser_notFound() {
        UserUpdateRequest upd = new UserUpdateRequest("noneexisting", "updatedTest", "new@example.com", "ADMIN");
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(upd));
    }

    /**
     * Should return only agent/admin users matching the query.
     */
    @Test
    void searchAgentAndAdminUsers() {
        List<UserResponse> list = userService.searchAgentAndAdminUsers("");
        assertEquals(3, list.size());
    }

    /**
     * Should return all users matching the query regardless of role.
     */
    @Test
    void searchAllUsers() {
        List<UserResponse> list = userService.searchAllUsers("a");
        assertEquals(4, list.size());
    }

    /**
     * Should retrieve a list of all users.
     */
    @Test
    void getAllUsers() {
        List<UserResponse> all = userService.getAllUsers();
        assertFalse(all.isEmpty());
    }

    /**
     * Should return tickets submitted by a user.
     */
    @Test
    void getTicketsByUserId_success() {
        User user = userRepository.findByUsername("AnotherTest").orElseThrow();

        List<TicketResponse> tickets = userService.getTicketsByUserId(user.getId());
        assertEquals(1, tickets.size());
        assertEquals(94, tickets.get(0).ticketId());
    }

    /**
     * Should throw not found exception for non-existing user ID.
     */
    @Test
    void getTicketsByUserId_notFound() {
        assertThrows(ResponseStatusException.class, () -> userService.getTicketsByUserId(846684L));
    }

    /**
     * Should return a user
     */
    @Test
    void getUserByUsername() throws Exception {
        assertTrue(userRepository.findByUsername("AnotherTest").isPresent());
    }
}
