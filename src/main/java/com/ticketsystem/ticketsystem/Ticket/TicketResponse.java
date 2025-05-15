package com.ticketsystem.ticketsystem.Ticket;

import java.time.LocalDateTime;

public record TicketResponse(
        Long ticketId,
        String shortDescription,
        String description,
        String ticketStatus,
        LocalDateTime createdAt,
        Long submittedById,
        String submittedByUsername,
        String assignedToUsername
) {}
