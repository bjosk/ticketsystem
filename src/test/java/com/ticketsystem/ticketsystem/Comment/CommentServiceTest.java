package com.ticketsystem.ticketsystem.Comment;

import com.ticketsystem.ticketsystem.TestUtils.EntityFactory;
import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketRepository;
import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void getCommentById_whenCommentDoesNotExist_thenThrowException() {

        // Stubs the repository
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the service and assert that a NoSuchElementException was thrown
        NoSuchElementException ex = assertThrows(
                NoSuchElementException.class,
                () -> commentService.getCommentById(anyLong())
        );

        // Verify that the service was called only once
        verify(commentRepository, times(1)).findById(anyLong());
    }

    @Test
    void addComment_whenTicketAndUserExists_thenCreateComment() throws Exception {
        // Arrange: build the request DTO with authorId=1, ticketId=25, and the comment text
        CommentRequest request = new CommentRequest(1L, 25L, "Dummy comment");

        // Create a fake User (id=1) and Ticket (id=25) owned by that user
        User user = EntityFactory.createUsers(1).get(0);
        Ticket ticket = EntityFactory.createTicket(25L, user);
        // Comment entity that the repository will return (with same ids)
        Comment comment = EntityFactory.createComment(25L, user, ticket);

        // Stub: userRepository.findById(1) → returns fake user
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // Stub: ticketRepository.findById(25) → returns fake ticket
        when(ticketRepository.findById(25L))
                .thenReturn(Optional.of(ticket));

        // Stub: Capture any Comment and return our prepared comment
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);

        // Act: call the service method
        CommentResponse resp = commentService.addComment(request);

        // Assert: verify the returned DTO’s fields match the saved comment and request
        assertEquals(comment.getId(),       resp.id(),             "Response ID should match saved Comment ID");
        assertEquals(request.ticketId(),    resp.ticketId(),       "Response ticketId should match request");
        assertEquals(request.authorId(),    resp.authorId(),       "Response authorId should match request");
        assertEquals(request.text(),        resp.text(),           "Response text should match request");
        assertEquals(user.getUsername(),    resp.authorUsername(), "Response authorUsername should match User.username");
    }

}
