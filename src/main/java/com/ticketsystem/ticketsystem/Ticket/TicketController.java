package com.ticketsystem.ticketsystem.Ticket;

import com.ticketsystem.ticketsystem.Comment.CommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets") //Base URL for all endpoints in this controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * Updates an existing ticket.
     * @param id      the unique identifier of the ticket to update
     * @param request the {@link TicketUpdateRequest} containing fields to modify
     */
    @PutMapping("/{id}")
    public void updateTicket(@PathVariable Long id, @RequestBody TicketUpdateRequest request) {
        ticketService.updateTicket(id, request);
    }

    /**
     * Retrieves all possible ticket statuses.
     * @return an array of {@link TicketStatus} enums representing each valid status
     */
    @GetMapping("/statuses")
    public TicketStatus[] getTicketStatuses() {
        return ticketService.getTicketStatuses();
    }

    /**
     * Retrieves all comments associated with a given ticket.
     *
     * @param ticketId the unique identifier of the ticket whose comments are requested
     * @return a list of {@link CommentResponse} DTOs for the specified ticket
     */
    @GetMapping("/{ticketId}/comments")
    public List<CommentResponse> getCommentsForTicket(@PathVariable Long ticketId) {
        return ticketService.getCommentsForTicket(ticketId);
    }

    /**
     * Creates a new ticket.
     * @param request the {@link TicketRequest} DTO containing the ticket details
     * @return a {@link TicketResponse} representing the newly created ticket
     */
    @PostMapping
    public TicketResponse createTicket(@RequestBody TicketRequest request) {
        return ticketService.createTicket(request);
    }

    /**
     * Retrieves all tickets.
     * @return a list of {@link TicketResponse} DTOs for all existing tickets
     */
    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }

    /**
     * Retrieves a single ticket by its unique identifier.
     * @param id the unique identifier of the ticket to retrieve
     * @return a {@link TicketResponse} containing the ticket’s details
     */
    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }
}
