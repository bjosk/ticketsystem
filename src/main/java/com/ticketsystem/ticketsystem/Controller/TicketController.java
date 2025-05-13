package com.ticketsystem.ticketsystem.Controller;

import com.ticketsystem.ticketsystem.Model.Ticket;
import com.ticketsystem.ticketsystem.Model.TicketStatus;
import com.ticketsystem.ticketsystem.Model.User;
import com.ticketsystem.ticketsystem.Repository.TicketRepository;
import com.ticketsystem.ticketsystem.Repository.UserRepository;
import com.ticketsystem.ticketsystem.dto.TicketRequest;
import com.ticketsystem.ticketsystem.dto.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/tickets") //Base URL for all endpoints in this controller
public class TicketController {
    //Repositories for accessing database operations
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new ticket submitted by a user
    @PostMapping
    public TicketResponse createTicket(@RequestBody TicketRequest request) {

        try {
            User user = userRepository.findById(request.userId()).orElseThrow();

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
                    ticket.getAssignedTo() != null ? ticket.getAssignedTo().getUsername() : null
            );
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + request.userId() + " not found");
        }
    }

    // Get all tickets
    @GetMapping
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

    //Get ticket by ticketId
    @GetMapping("/{id}")
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


}
