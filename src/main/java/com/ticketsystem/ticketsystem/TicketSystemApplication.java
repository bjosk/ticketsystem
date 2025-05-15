package com.ticketsystem.ticketsystem;

import com.ticketsystem.ticketsystem.Comment.Comment;
import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketStatus;
import com.ticketsystem.ticketsystem.Comment.CommentRepository;
import com.ticketsystem.ticketsystem.Ticket.TicketRepository;
import com.ticketsystem.ticketsystem.User.Role;
import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketSystemApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    public static void main(String[] args) {
        SpringApplication.run(TicketSystemApplication.class, args);
    }
}
