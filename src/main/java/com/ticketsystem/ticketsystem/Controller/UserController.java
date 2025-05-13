package com.ticketsystem.ticketsystem.Controller;

import com.ticketsystem.ticketsystem.Model.Ticket;
import com.ticketsystem.ticketsystem.Model.User;
import com.ticketsystem.ticketsystem.Repository.UserRepository;
import com.ticketsystem.ticketsystem.dto.TicketResponse;
import com.ticketsystem.ticketsystem.dto.UserRequest;
import com.ticketsystem.ticketsystem.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    // Create a new user (register)
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {

        //Check if username or email is taken. If taken an exception is thrown.
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setEmail(request.email());

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getRole().name(),
                saved.getSubmittedTickets()
                        .stream()
                        .map(Ticket::getTicketId)
                        .toList()

        );
    }

    // Get all users
    @GetMapping
    public List<UserResponse> getAllUsers(){
        return ((List<User>) userRepository.findAll()).stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name(),
                        user.getSubmittedTickets()
                                .stream()
                                .map(Ticket::getTicketId)
                                .toList()

                ))
                .toList();
    }

    // Get tickets by userId
    @GetMapping("/{userId}/tickets")
    public List<TicketResponse> getTicketsByUserId(@PathVariable Long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found");
        }
        List<Ticket> userTickets = userRepository.findById(userId)
                .orElseThrow()
                .getSubmittedTickets();

        return userTickets.stream()
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

}
