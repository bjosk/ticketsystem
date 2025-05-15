package com.ticketsystem.ticketsystem.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String text,
        LocalDateTime createdAt,
        Long authorId,
        String authorUsername,
        Long ticketId
) {}
