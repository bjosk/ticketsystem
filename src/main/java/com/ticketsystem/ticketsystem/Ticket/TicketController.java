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

    @GetMapping("/{ticketId}/comments")
    public List<CommentResponse> getCommentsForTicket(@PathVariable Long ticketId) {
        return ticketService.getCommentsForTicket(ticketId);
    }

    @PostMapping
    public TicketResponse createTicket(@RequestBody TicketRequest request) {
        return ticketService.createTicket(request);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }
}
