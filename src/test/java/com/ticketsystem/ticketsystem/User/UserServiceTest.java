package com.ticketsystem.ticketsystem.User;

import com.ticketsystem.ticketsystem.TestUtils.EntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_ReturnsAllUsers() throws Exception {
        List<User> fakeUsers = EntityFactory.createUsers(10);
        when(userRepository.findAll()).thenReturn(fakeUsers);

        List<UserResponse> userResponses = userService.getAllUsers();

        assertNotNull(userResponses);
        assertEquals(fakeUsers.size(), userResponses.size());

        for (int i = 0; i < 10; i++) {
            assertEquals(fakeUsers.get(i).getId(), userResponses.get(i).userId());
            assertEquals(fakeUsers.get(i).getUsername(), userResponses.get(i).username());
            assertEquals(fakeUsers.get(i).getEmail(), userResponses.get(i).email());
            assertEquals(fakeUsers.get(i).getRole(), Role.valueOf(userResponses.get(i).role()));
            assertEquals(fakeUsers.get(i).getSubmittedTickets().size(), userResponses.get(i).submittedTicketIds().size());
            System.out.println(fakeUsers.get(i).getSubmittedTickets());
        }

        // Verify repository was called exactly once with correct arguments
        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    void searchAllUsers_returnsUsersWithAnyRole() throws Exception {
        // Create fake users
        User admin = EntityFactory.createUserWithTickets(1L, "adminUser", "admin@example.com", Role.ADMIN, 101L, 102L);
        User agent = EntityFactory.createUserWithTickets(2L, "agentUser", "agent@example.com", Role.AGENT, 103L, 104L);
        User regular = EntityFactory.createUserWithTickets(3L, "regularUser", "regular@example.com", Role.USER, 105L, 106L);

        // The repository query should return users of any role. "us" is the search query
        when(userRepository.findUsersByUsernameContainsIgnoreCase("us")).thenReturn(Arrays.asList(admin, agent, regular));

        //Act
        List<UserResponse> responses = userService.searchAllUsers("us");

        // Verify three results
        assertEquals(3, responses.size());

        // Map responses by ID for easier assertions
        Map<Long, UserResponse> map = new HashMap<>();
        for (UserResponse ur : responses) {
            map.put(ur.userId(), ur);
        }

        // Check ADMIN entry
        UserResponse adminResp = map.get(1L);
        assertNotNull(adminResp);
        assertEquals("adminUser", adminResp.username());
        assertEquals("admin@example.com", adminResp.email());
        assertEquals(Role.ADMIN.name(), adminResp.role());
        List<Long> adminTickets = adminResp.submittedTicketIds();
        // Check if the user has the correct submitted tickets
        assertEquals(2, adminTickets.size());
        assertTrue(adminTickets.contains(101L), "Should contain ticketId 101");
        assertTrue(adminTickets.contains(102L), "Should contain ticketId 102");

        // Check AGENT entry
        UserResponse agentResp = map.get(2L);
        assertNotNull(agentResp);
        assertEquals("agentUser", agentResp.username());
        assertEquals("agent@example.com", agentResp.email());
        assertEquals(Role.AGENT.name(), agentResp.role());

        // Check if the user has the correct submitted tickets
        List<Long> agentTickets = agentResp.submittedTicketIds();
        assertEquals(2, agentTickets.size());
        assertTrue(agentTickets.contains(103L), "Should contain ticketId 103");
        assertTrue(agentTickets.contains(104L), "Should contain ticketId 104");

        // Check regular USER entry
        UserResponse regularResp = map.get(3L);
        assertNotNull(regularResp);
        assertEquals("regularUser", regularResp.username());
        assertEquals("regular@example.com", regularResp.email());
        assertEquals(Role.USER.name(), regularResp.role());
        // Check if the user has the correct submitted tickets
        List<Long> regularTickets = regularResp.submittedTicketIds();
        assertEquals(2, regularTickets.size());
        assertTrue(regularTickets.contains(105L), "Should contain ticketId 103");
        assertTrue(regularTickets.contains(106L), "Should contain ticketId 104");

        // Verify repository was called exactly once with correct arguments
        verify(userRepository, times(1))
                .findUsersByUsernameContainsIgnoreCase("us");
    }

    @Test
    void searchAgentAndAdminUsers_returnsOnlyAdminAndAgentUsers() throws Exception {
        // Arrange
        // 1) Build three fake User instances: one USER, one ADMIN, one AGENT
        User admin = EntityFactory.createUserWithTickets(1L, "adminUser", "admin@example.com", Role.ADMIN, 101L, 102L);
        User agent = EntityFactory.createUserWithTickets(2L, "agentUser", "agent@example.com", Role.AGENT, 103L, 104L);

        // The repository query should return only ADMIN and AGENT (Role != USER). "us" is the search query
        when(userRepository.findUsersByUsernameContainsIgnoreCaseAndRoleIsNot("us", Role.USER))
                .thenReturn(List.of(admin, agent));

        // Act
        List<UserResponse> responses = userService.searchAgentAndAdminUsers("us");

        // Assert: verify two results
        assertEquals(2, responses.size());

        // Map responses by ID for easier assertions
        Map<Long, UserResponse> map = new HashMap<>();
        for (UserResponse ur : responses) {
            map.put(ur.userId(), ur);
        }

        // Check ADMIN entry
        UserResponse adminResp = map.get(1L);
        assertNotNull(adminResp);
        assertEquals("adminUser", adminResp.username());
        assertEquals("admin@example.com", adminResp.email());
        assertEquals(Role.ADMIN.name(), adminResp.role());
        List<Long> adminTickets = adminResp.submittedTicketIds();
        // Check if the user has the correct submitted tickets
        assertEquals(2, adminTickets.size());
        assertTrue(adminTickets.contains(101L), "Should contain ticketId 101");
        assertTrue(adminTickets.contains(102L), "Should contain ticketId 102");

        // Check AGENT entry
        UserResponse agentResp = map.get(2L);
        assertNotNull(agentResp);
        assertEquals("agentUser", agentResp.username());
        assertEquals("agent@example.com", agentResp.email());
        assertEquals(Role.AGENT.name(), agentResp.role());
        // Check if the user has the correct submitted tickets
        List<Long> agentTickets = agentResp.submittedTicketIds();
        assertEquals(2, agentTickets.size());
        assertTrue(agentTickets.contains(103L), "Should contain ticketId 103");
        assertTrue(agentTickets.contains(104L), "Should contain ticketId 104");

        // Verify repository was called exactly once with correct arguments
        verify(userRepository, times(1))
                .findUsersByUsernameContainsIgnoreCaseAndRoleIsNot("us", Role.USER);
    }

    @Test
    void updateUser_whenUserFound_updatesFieldsAndSaves() throws Exception {
        // Arrange - Build a request that changes username, email, and role
        UserUpdateRequest req = new UserUpdateRequest(
                "bob",  // Existing user
                "newBob",               // New username
                "newbob@example.com",   // New email
                "ADMIN"                 // New role
        );

        // Create a fake user to return from the repository
        User existing = new User();
        existing.setUsername(req.selectedUsername());   // Original username
        existing.setEmail("oldbob@example.com");        // Original email
        existing.setRole(Role.USER);                    // Original role
        // The id is autogenerated and does not have a getter.
        // Reflectively set id on existing user (so getId() returns something non-null)
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(existing, 77L);

        // Stub the repository so that findByUsername("bob") returns our existing user
        when(userRepository.findByUsername(req.selectedUsername()))
                .thenReturn(Optional.of(existing));

        // Stub save(...) to just return the same instance that was passed in
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service
        userService.updateUser(req);

        // Assert: existing fields were modified
        assertEquals("newBob", existing.getUsername());
        assertEquals("newbob@example.com", existing.getEmail());
        assertEquals(Role.ADMIN, existing.getRole());

        // Verify the methods were only called once
        verify(userRepository, times(1)).findByUsername(req.selectedUsername());
        verify(userRepository, times(1)).save(existing);
    }


    @Test
    void createUser_whenUsernameExists_throwsConflict() {
        // Arrange - simulate a database check that finds the username already in use.
        String taken = "alice";
        when(userRepository.existsByUsername(taken)).thenReturn(true);

        UserRequest req = new UserRequest(
                taken,
                "anyPassword",
                "alice@example.com"
        );

        // Act & Assert - Calls the function. Fails if it does not return a ResponseStatusException
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.createUser(req)
        );
        // Verify that the exception’s status code and message is correct.
        assertEquals(409, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertEquals("Username is already taken", ex.getReason());

        // Verify that save(...) was never called
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_whenEmailExists_throwsConflict() {
        // Arrange - simulate a database check that finds the username is not in use and email already in use
        UserRequest req = new UserRequest(
                "newAlice",
                "irrelevantPassword",
                "alice@example.com"
        );

        // 1. Username does not exist
        when(userRepository.existsByUsername(req.username())).thenReturn(false);
        // 2. email does exist
        when(userRepository.existsByEmail(req.email())).thenReturn(true);

        // Act & Assert - // Verify that the exception’s status code and message is correct.
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.createUser(req)
        );

        assertEquals(409, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertEquals("Email is already in use", ex.getReason());

        // Verify that save(...) was never called
        verify(userRepository, never()).save(any(User.class));
    }
}

