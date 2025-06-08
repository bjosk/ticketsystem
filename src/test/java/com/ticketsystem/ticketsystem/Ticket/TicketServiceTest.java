package com.ticketsystem.ticketsystem.Ticket;

import com.ticketsystem.ticketsystem.Comment.Comment;
import com.ticketsystem.ticketsystem.Comment.CommentResponse;
import com.ticketsystem.ticketsystem.TestUtils.EntityFactory;
import com.ticketsystem.ticketsystem.User.Role;
import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private List<User> users;
    private Ticket ticketWithComments;
    private Ticket ticketWithoutComments;
    private Comment commentA;
    private Comment commentB;

    /**
     * Prepares test fixtures
     */
    @BeforeEach
    void setUp() throws Exception {
        users = EntityFactory.createUsers(5);

        // Ticket 900 has two comments; ticket 901 has none
        ticketWithComments    = EntityFactory.createTicket(900L, users.get(0));
        ticketWithoutComments = EntityFactory.createTicket(901L, users.get(1));

        commentA = EntityFactory.createComment(1000L, users.get(0), ticketWithComments);
        commentB = EntityFactory.createComment(1001L, users.get(0), ticketWithComments);

        // attach comments to the ticket
        ticketWithComments.setComments(List.of(commentA, commentB));
        ticketWithoutComments.setComments(Collections.emptyList());
    }

    /**
     * Verifies that calling {@code updateTicket} with a non-existent ticket ID
     * throws a 404 {@link ResponseStatusException} and does not attempt to save.
     */
    @Test
    void updateTicket_whenTicketDoesntExist_shouldThrowException() throws Exception {

        when(ticketRepository.findById(900L))
                .thenReturn(Optional.empty());

        TicketUpdateRequest req = new TicketUpdateRequest(
                "IN_PROGRESS",          // ticketStatus
                "newAgent",
                "newShort",
                "newLongDesc"
        );

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> ticketService.updateTicket(900L, req)
        );
        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Ticket not found", ex.getReason());

        verify(ticketRepository, times(1)).findById(900L);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    /**
     * Verifies that calling {@code updateTicket} with an unknown assignee
     * throws a 404 {@link ResponseStatusException} and does not save changes.
     */
    @Test
    void updateTicket_whenUserDoesntExist_shouldThrowException() throws Exception {
        when(ticketRepository.findById(900L)).thenReturn(Optional.of(ticketWithComments));

        TicketUpdateRequest req = new TicketUpdateRequest(
                "IN_PROGRESS",
                "ghost",
                "newShort",
                "newLongDesc"
        );

        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> ticketService.updateTicket(900L, req)
        );
        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Assigned user not found", ex.getReason());

        // Lookup should have happened once:
        verify(ticketRepository, times(1)).findById(900L);

        // Looked up the user exactly once:
        verify(userRepository, times(1)).findByUsername("ghost");

        // No save
        verify(ticketRepository, never()).save(any(Ticket.class));


    }

    /**
     * Verifies that {@code getCommentsForTicket} returns mapped DTOs
     * when the ticket exists and has comments.
     */
    @Test
    void getCommentsForTicket_whenTicketExists_shouldReturnComments() throws ReflectiveOperationException {
        // Stub repository
        when(ticketRepository.findById(900L))
                .thenReturn(Optional.of(ticketWithComments));

        // Call service
        List<CommentResponse> responses =
                ticketService.getCommentsForTicket(900L);

        // Expected list of comments
        List<Comment> expected = List.of(commentA, commentB);

        assertEquals(expected.size(), responses.size());

        // loop over each and assert fields
        for (int i = 0; i < expected.size(); i++) {
            CommentResponse r = responses.get(i);
            Comment exp = expected.get(i);

            assertEquals(exp.getId(),           r.id(),             "commentId mismatch at index " + i);
            assertEquals(exp.getText(),         r.text(),           "text mismatch at index " + i);
            assertEquals(exp.getCreatedAt(),    r.createdAt(),      "createdAt mismatch at index " + i);
            assertEquals(exp.getAuthor().getId(),       r.authorId(),       "authorId mismatch at index " + i);
            assertEquals(exp.getAuthor().getUsername(), r.authorUsername(), "authorUsername mismatch at index " + i);
            assertEquals(ticketWithComments.getTicketId(), r.ticketId(),    "ticketId mismatch at index " + i);
        }


        verify(ticketRepository, times(1)).findById(900L);

    }

    /**
     * Verifies that {@code getCommentsForTicket} throws a 404
     * when the ticket is not found.
     */
    @Test
    void getCommentsForTicket_whenTicketNotFound_throwsNotFound() {
        // Arrange: repository returns no ticket
        when(ticketRepository.findById(900L))
                .thenReturn(Optional.empty());

        // Act & Assert: service should throw a 404 with the correct message
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> ticketService.getCommentsForTicket(900L)
        );

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Ticket with ID 900 not found", ex.getReason());

        // Verify that we did call findById exactly once
        verify(ticketRepository, times(1)).findById(900L);
    }

    /**
     * Verifies that {@code getTicketStatuses} returns all enum values.
     */
    @Test
    void getTicketStatuses_returnsAllEnumValues() {
        TicketStatus[] actual = ticketService.getTicketStatuses();
        assertArrayEquals(TicketStatus.values(), actual);
    }

    /**
     * Verifies that {@code createTicket} returns a correct {@link TicketResponse}
     * when the submitting user exists.
     */
    @Test
    void createTicket_whenUserExists_shouldReturnTicketResponse() throws Exception {
        // Arrange: build a TicketRequest and build a fake User
        User fakeUser = EntityFactory.createUserWithTickets(10L, "Alice", "alice@example.com", Role.USER);

        TicketRequest request = new TicketRequest(
                fakeUser.getUsername(),
                "dummy",
                "dummy"
        );

        when(userRepository.findByUsername(fakeUser.getUsername()))
                .thenReturn(Optional.of(fakeUser));

        Ticket ticketReturnedBySave = EntityFactory.createTicket(1273L, fakeUser);

        // Stub ticketRepository.save(...) to return a Ticket with an ID and fields set
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticketReturnedBySave);

        // Act: call the service
        TicketResponse response = ticketService.createTicket(request);

        // Assert: verify the returned TicketResponse has the correct fields
        assertNotNull(response);
        assertEquals(1273L, response.ticketId());
        assertEquals("dummy", response.shortDescription());
        assertEquals("dummy", response.description());
        assertEquals("NEW", response.ticketStatus());
        assertEquals(10L, response.submittedById());
        assertEquals("Alice", response.submittedByUsername());
        assertNull(response.assignedToUsername());

        // Finally, verify ticketRepository.save(...) was called exactly once
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

}