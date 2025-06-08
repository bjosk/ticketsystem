package com.ticketsystem.ticketsystem.Comment;

import com.ticketsystem.ticketsystem.Ticket.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * Retrieves a single comment by its unique identifier.
     *
     * @param ticketId the ID of the comment to retrieve
     * @return the {@link CommentResponse} containing comment details
     */
    @GetMapping("/{ticketId}")
    public CommentResponse getCommentById(@PathVariable long ticketId) {
        return commentService.getCommentById(ticketId);
    }

    /**
     * Creates a new comment.
     *
     * @param request the {@link CommentRequest} containing the content and metadata of the comment to add
     * @return the {@link CommentResponse} representing the newly created comment
     */
    @PostMapping
    public CommentResponse addComment(@RequestBody CommentRequest request) {
        return commentService.addComment(request);
    }
}
