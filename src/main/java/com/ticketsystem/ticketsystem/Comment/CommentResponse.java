package com.ticketsystem.ticketsystem.Comment;

import java.time.LocalDateTime;

/**
 * Data transfer object for returning comment details to the client.
 * @param id               the unique identifier of the comment
 * @param text             the text content of the comment
 * @param createdAt        the timestamp when the comment was created
 * @param authorId         the unique identifier of the comment’s author
 * @param authorUsername   the username of the comment’s author
 * @param ticketId         the unique identifier of the ticket this comment is associated with
 */
public record CommentResponse(
        Long id,
        String text,
        LocalDateTime createdAt,
        Long authorId,
        String authorUsername,
        Long ticketId
) {}
