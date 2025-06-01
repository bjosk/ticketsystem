package com.ticketsystem.ticketsystem.Ticket;

public record TicketUpdateRequest(String ticketStatus, String assignedTo, String shortDescription, String description) {
}
