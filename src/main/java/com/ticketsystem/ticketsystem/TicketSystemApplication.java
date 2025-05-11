package com.ticketsystem.ticketsystem;

import com.ticketsystem.ticketsystem.Model.*;
import com.ticketsystem.ticketsystem.Repository.CommentRepository;
import com.ticketsystem.ticketsystem.Repository.TicketRepository;
import com.ticketsystem.ticketsystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class TicketSystemApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    public static void main(String[] args) {
        SpringApplication.run(TicketSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Create and save a user
        User user = new User();
        user.setUsername("alice");
        user.setPassword("password");
        user.setEmail("alice@example.com");
        user.setRole(Role.USER);
        userRepository.save(user);

        // Create and save a ticket submitted by the user
        Ticket ticket = new Ticket();
        ticket.setShortDescription("VPN not working");
        ticket.setDescription("Can't connect to VPN since yesterday.");
        ticket.setTicketStatus(TicketStatus.NEW);
        ticket.setSubmittedBy(user);
        ticketRepository.save(ticket);

        // Create and save a comment on the ticket
        Comment comment = new Comment();
        comment.setText("This issue has been affecting multiple users.");
        comment.setAuthor(user);
        comment.setTicket(ticket);
        commentRepository.save(comment);

        // Fetch and print everything
        System.out.println("User: " + userRepository.findById(user.getId()).orElse(null));
        System.out.println("Ticket: " + ticketRepository.findById(ticket.getId()).orElse(null));
        System.out.println("Comment: " + commentRepository.findById(comment.getId()).orElse(null));
    }
}
