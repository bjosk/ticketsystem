package com.ticketsystem.ticketsystem.Ticket;

import com.ticketsystem.ticketsystem.User.UserRequest;
import com.ticketsystem.ticketsystem.User.UserResponse;
import com.ticketsystem.ticketsystem.User.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class TicketServiceIntegrationTest {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Test
    void createTicket() {
        TicketRequest ticketRequest = new TicketRequest(
                "Jane",
                "Problem with PC",
                "Cannot log in to the computer when it starts up"
        );

        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);

        // Assert response fields
        assertNotNull(ticketResponse.ticketId(),      "Ticket ID should be generated");
        assertEquals(ticketRequest.shortDescription(), ticketResponse.shortDescription());
        assertEquals(ticketRequest.description(), ticketResponse.description());
        assertEquals("NEW", ticketResponse.ticketStatus());
        assertNotNull(ticketResponse.createdAt(),     "createdAt must be set");
        assertEquals(ticketRequest.username(), ticketResponse.submittedByUsername());
        assertNull(ticketResponse.assignedToUsername(), "New tickets should be unassigned");

        // Assert persistence
        var optEntity = ticketRepository.findById(ticketResponse.ticketId());  // assuming you expose findById
        assertTrue(optEntity.isPresent(), "Ticket must be in the database");

        var entity = optEntity.get();
        assertNotNull(entity.getTicketId(), "Ticket ID must be set");
        assertEquals(ticketRequest.shortDescription(),          entity.getShortDescription());
        assertEquals(ticketRequest.description(),               entity.getDescription());
        assertEquals(TicketStatus.NEW,                          entity.getTicketStatus());
        assertEquals(ticketRequest.username(),                  entity.getSubmittedBy().getUsername());
        assertNull(entity.getAssignedTo(),             "assignedTo should remain null");
//
        ticketRepository.deleteById(entity.getTicketId());
    }
}
