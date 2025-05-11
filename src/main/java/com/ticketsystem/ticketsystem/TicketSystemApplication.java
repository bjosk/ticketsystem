package com.ticketsystem.ticketsystem;

import com.ticketsystem.ticketsystem.Model.Role;
import com.ticketsystem.ticketsystem.Model.Ticket;
import com.ticketsystem.ticketsystem.Model.TicketStatus;
import com.ticketsystem.ticketsystem.Model.User;
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

    public static void main(String[] args) {
        SpringApplication.run(TicketSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Create and save a user
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("alice@example.com");
        user.setRole(Role.USER);

        userRepository.save(user);

        // Retrieve user by username
        User found = userRepository.findByUsername("test");
        System.out.println("Found user: " + found);

        // Create and save a ticket
        Ticket ticket = new Ticket();
        ticket.setShortDescription("Printer not working");
        ticket.setDescription("The office printer is jammed.");
        ticket.setTicketStatus(TicketStatus.NEW);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setSubmittedBy(user);

        ticketRepository.save(ticket);

        Ticket savedTicket = ticketRepository.findById(ticket.getId()).orElse(null);
        System.out.println("Saved Ticket: " + savedTicket.toString());


    }
}
