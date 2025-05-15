package com.ticketsystem.ticketsystem.Comment;

import com.ticketsystem.ticketsystem.Ticket.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{ticketId}")
    public CommentResponse getCommentById(@PathVariable long ticketId) {
        return commentService.getCommentById(ticketId);
    }

    @PostMapping
    public CommentResponse addComment(@RequestBody CommentRequest request) {
        return commentService.addComment(request);
    }
}
