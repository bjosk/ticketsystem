package com.ticketsystem.ticketsystem.User;

import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    public UserResponse loadOrCreateUser(Authentication auth) {
//        Jwt jwt = (Jwt) auth.getPrincipal();
//        String externalId = jwt.getSubject(); // e.g., "auth0|abc123"
//        String email = jwt.getClaim("email");
//        String username = jwt.getClaim("username");
//
//        // Try to find existing user
//        User user = userRepository.findByExternalId(externalId).orElseGet(() -> {
//            // Create new user
//            User newUser = new User();
//            newUser.setUsername(username); // or derive from another claim
//            newUser.setEmail(email);
//            newUser.setExternalId(externalId);
//            return userRepository.save(newUser);
//        });
//
//        // Convert to DTO
//        return new UserResponse(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getRole().name(),
//                user.getSubmittedTickets()
//                        .stream()
//                        .map(Ticket::getTicketId)
//                        .toList()
//        );
//    }



    public UserResponse createUser(UserRequest request) {
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

    public List<UserResponse> getAllUsers() {
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

    public List<TicketResponse> getTicketsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " not found"));

        return user.getSubmittedTickets()
                .stream()
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
