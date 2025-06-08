package com.ticketsystem.ticketsystem.Comment;

import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketRepository;
import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.User.UserRepository;
import com.ticketsystem.ticketsystem.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    /**
     * Retrieves a single comment by its unique identifier.
     * @param id the unique identifier of the comment to retrieve
     * @return a {@link CommentResponse} containing the comment’s details
     * @throws java.util.NoSuchElementException if no comment exists with the given ID
     */
    public CommentResponse getCommentById(@PathVariable Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();

        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getAuthor().getId(),
                comment.getAuthor().getUsername(),
                comment.getTicket().getTicketId()
        );
    }

    /**
     * Creates a new comment on a ticket.
     * @param request the {@link CommentRequest} containing: authorId, ticketId, and text
     * @return a {@link CommentResponse} representing the newly created comment, including its assigned ID,
     *         creation timestamp, and author/ticket references
     * @throws ResponseStatusException with HTTP 404 (NOT_FOUND) if the specified user or ticket does not exist
     */
    public CommentResponse addComment(CommentRequest request) {
        User author = userRepository.findById(request.authorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Ticket ticket = ticketRepository.findById(request.ticketId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        Comment comment = new Comment();
        comment.setText(request.text());
        comment.setAuthor(author);
        comment.setTicket(ticket);

        Comment saved = commentRepository.save(comment);

        return new CommentResponse(
                saved.getId(),
                saved.getText(),
                saved.getCreatedAt(),
                saved.getAuthor().getId(),
                saved.getAuthor().getUsername(),
                saved.getTicket().getTicketId()
        );
    }
}
