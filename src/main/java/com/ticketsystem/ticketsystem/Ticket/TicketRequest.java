package com.ticketsystem.ticketsystem.Ticket;
/**
 * A simple data transfer object (DTO) for creating a ticket
 */
public record TicketRequest(String username, String shortDescription, String description) {
}

