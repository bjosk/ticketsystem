package com.ticketsystem.ticketsystem.Ticket;

import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void getTicketStatuses_returnsAllEnumValues() {
        TicketStatus[] actual = ticketService.getTicketStatuses();
        assertArrayEquals(TicketStatus.values(), actual);
    }

    @Test
    void createTicket_whenUserExists_shouldReturnTicketResponse() throws Exception {
        // Arrange: build a TicketRequest
        String username = "alice";
        TicketRequest request = new TicketRequest(
                username,
                "Cannot log in",
                "I get a 500 error when submitting login form"
        );

        // 1) Build a fake User and set its private 'id' via reflection
        User fakeUser = new User();
        fakeUser.setUsername(username);
        fakeUser.setPassword("irrelevant");
        fakeUser.setEmail("alice@example.com");
        // Reflectively set id = 10L
        var idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(fakeUser, 10L);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(fakeUser));

        // 2) Stub ticketRepository.save(...) to return a Ticket with an ID and fields set
        Ticket savedTicket = new Ticket();
        // Reflectively set ticketId = 123L
        var ticketIdField = Ticket.class.getDeclaredField("ticketId");
        ticketIdField.setAccessible(true);
        ticketIdField.set(savedTicket, 123L);

        savedTicket.setShortDescription(request.shortDescription());
        savedTicket.setDescription(request.description());
        savedTicket.setTicketStatus(TicketStatus.NEW);
        savedTicket.setSubmittedBy(fakeUser);
        // Simulate @PrePersist effect
        var createdAtField = Ticket.class.getDeclaredField("createdAt");
        createdAtField.setAccessible(true);
        createdAtField.set(savedTicket, LocalDateTime.now());

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(savedTicket);

        // Act: call the service
        TicketResponse response = ticketService.createTicket(request);

        // Assert: verify the returned TicketResponse has the correct fields
        assertNotNull(response);
        assertEquals(123L, response.ticketId());
        assertEquals("Cannot log in", response.shortDescription());
        assertEquals("I get a 500 error when submitting login form", response.description());
        assertEquals("NEW", response.ticketStatus());
        assertEquals(10L, response.submittedById());
        assertEquals("alice", response.submittedByUsername());
        assertNull(response.assignedToUsername());

        // Finally, verify ticketRepository.save(...) was called exactly once
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

}