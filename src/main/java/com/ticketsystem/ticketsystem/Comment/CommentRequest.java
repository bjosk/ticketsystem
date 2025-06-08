package com.ticketsystem.ticketsystem.Comment;

/**
 * Data transfer object for creating or updating a comment.
 * @param authorId the ID of the user authoring the comment
 * @param ticketId the ID of the ticket to which the comment belongs
 * @param text     the text content of the comment
 */
public record CommentRequest(
        Long authorId,     // the author of the comment
        Long ticketId,   // the ticket being commented on
        String text
) {}
