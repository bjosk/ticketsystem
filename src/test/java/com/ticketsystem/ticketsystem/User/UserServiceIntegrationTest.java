package com.ticketsystem.ticketsystem.User;

import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketRepository;
import com.ticketsystem.ticketsystem.Ticket.TicketStatus;
import jakarta.transaction.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void createUser() throws Exception {
        UserRequest userRequest1 = new UserRequest("JohnDoe", "12345", "johndoe@example.com");
//        UserRequest userRequest2 = new UserRequest("Jane", "5678", "jane@example.com");
//        UserRequest userRequest3 = new UserRequest("Roberto99", "mypass", "robertoju@example.com");
        UserResponse userResponse1 = userService.createUser(userRequest1);
//        UserResponse userResponse2 = userService.createUser(userRequest2);
//        UserResponse userResponse3 = userService.createUser(userRequest3);

        assertEquals(userResponse1.username(), userRequest1.username());
        assertTrue(userRepository.findByUsername("JohnDoe").isPresent());

    }

    @Test
    void getUserByUsername() throws Exception {
        assertTrue(userRepository.findByUsername("anotherTest").isPresent());
    }
}
