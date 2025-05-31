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

    public TicketStatus[] getTicketStatuses() {
        return TicketStatus.values();
    }

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

    // Get all tickets
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

    // Create a new ticket submitted by a user
    public TicketResponse createTicket(TicketRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + request.userId() + " not found"));

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
