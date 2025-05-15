package com.ticketsystem.ticketsystem.Ticket;
/**
 * A simple data transfer object (DTO) for creating a ticket
 */
public record TicketRequest(Long userId, String shortDescription, String description) {
}

