package com.ticketsystem.ticketsystem.dto;
/**
 * A simple data transfer object (DTO) for creating a ticket
 */
public record TicketRequest(Long userId, String shortDescription, String description) {
}

