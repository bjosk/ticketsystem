package com.ticketsystem.ticketsystem.Comment;

public record CommentRequest(
        Long authorId,     // the author of the comment
        Long ticketId,   // the ticket being commented on
        String text
) {}
