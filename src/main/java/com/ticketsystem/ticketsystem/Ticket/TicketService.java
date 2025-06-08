package com.ticketsystem.ticketsystem.Ticket;

import com.ticketsystem.ticketsystem.Comment.CommentResponse;
import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TicketService{

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Updates the specified ticket with provided fields.
     * @param ticketId the unique identifier of the ticket to update
     * @param request  the {@link TicketUpdateRequest} containing new values
     * @throws ResponseStatusException with HTTP 404 (NOT_FOUND) if the ticket or assigned user is not found
     */
    public void updateTicket(Long ticketId, TicketUpdateRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        if (request.ticketStatus() != null) {
            ticket.setTicketStatus(TicketStatus.valueOf(request.ticketStatus()));
        }

        if (request.assignedTo() != null) {
            User assignedUser = userRepository.findByUsername(request.assignedTo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assigned user not found"));
            ticket.setAssignedTo(assignedUser);
        }

        if (request.shortDescription() != null) {
            ticket.setShortDescription(request.shortDescription());
        }

        if (request.description() != null) {
            ticket.setDescription(request.description());
        }

        ticketRepository.save(ticket);
    }

    /**
     * Returns all valid ticket statuses.
     * @return an array of {@link TicketStatus} enums representing every possible status
     */
    public TicketStatus[] getTicketStatuses() {
        return TicketStatus.values();
    }

    /**
     * Retrieves all comments associated with the given ticket.
     * @param ticketId the unique identifier of the ticket whose comments are requested
     * @return a list of {@link CommentResponse} DTOs for each comment on the ticket
     * @throws ResponseStatusException with HTTP 404 (NOT_FOUND) if no ticket exists with the given ID
     */
    public List<CommentResponse> getCommentsForTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket with ID " + ticketId + " not found"));

        return ticket.getComments()
                .stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getText(),
                        comment.getCreatedAt(),
                        comment.getAuthor().getId(),
                        comment.getAuthor().getUsername(),
                        comment.getTicket().getTicketId()
                ))
                .toList();
    }

    /**
     * Retrieves a ticket by its unique identifier.
     * @param id the unique identifier of the ticket to retrieve
     * @return a {@link TicketResponse} containing the ticket’s details,
     *         including ID, descriptions, status, timestamps, submitter, and assignee
     * @throws ResponseStatusException with HTTP 404 (NOT_FOUND) if no ticket exists with the given ID
     */
    public TicketResponse getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        return new TicketResponse(
                ticket.getTicketId(),
                ticket.getShortDescription(),
                ticket.getDescription(),
                ticket.getTicketStatus().name(),
                ticket.getCreatedAt(),
                ticket.getSubmittedBy().getId(),
                ticket.getSubmittedBy().getUsername(),
                ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null
        );
    }

    /**
     * Retrieves all existing tickets.
     * @return a list of {@link TicketResponse} DTOs for every ticket in the system
     */
    public List<TicketResponse> getAllTickets() {
        return ((List<Ticket>) ticketRepository.findAll()).stream()
                .map(ticket -> new TicketResponse(
                        ticket.getTicketId(),
                        ticket.getShortDescription(),
                        ticket.getDescription(),
                        ticket.getTicketStatus().name(),
                        ticket.getCreatedAt(),
                        ticket.getSubmittedBy().getId(),
                        ticket.getSubmittedBy().getUsername(),
                        ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null
                ))
                .toList();
    }


    /**
     * Creates a new ticket submitted by a user.
     * @param request the {@link TicketRequest} containing username, shortDescription, and description
     * @return a {@link TicketResponse} representing the newly created ticket,
     *         including its generated ID, timestamps, and associations
     * @throws ResponseStatusException with HTTP 404 (NOT_FOUND) if the submitting user is not found
     */

    public TicketResponse createTicket(TicketRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + request.username() + " not found"));

        Ticket ticket = new Ticket();
        ticket.setShortDescription(request.shortDescription());
        ticket.setDescription(request.description());
        ticket.setTicketStatus(TicketStatus.NEW);
        ticket.setSubmittedBy(user);

        Ticket saved = ticketRepository.save(ticket);

        return new TicketResponse(
                saved.getTicketId(),
                saved.getShortDescription(),
                saved.getDescription(),
                saved.getTicketStatus().name(),
                saved.getCreatedAt(),
                saved.getSubmittedBy().getId(),
                saved.getSubmittedBy().getUsername(),
                saved.getAssignedTo() != null ? saved.getAssignedTo().getUsername() : null
        );
    }
}
