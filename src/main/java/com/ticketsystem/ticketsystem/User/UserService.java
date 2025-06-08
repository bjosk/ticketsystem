package com.ticketsystem.ticketsystem.User;

import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Updates an existing user’s details.
     * @param userUpdateRequest the {@link UserUpdateRequest} containing selectedUsername, newUsername, role, and email
     * @throws ResponseStatusException with HTTP 404 (NOT_FOUND) if no user exists with the selected username
     */
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findByUsername(userUpdateRequest.selectedUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!userUpdateRequest.newUsername().isEmpty()) {
            user.setUsername(userUpdateRequest.newUsername());
        }

        if (!userUpdateRequest.role().isEmpty()) {
            user.setRole(Role.valueOf(userUpdateRequest.role()));
        }

        if (!userUpdateRequest.email().isEmpty()) {
            user.setEmail(userUpdateRequest.email());
        }

        userRepository.save(user);
    }

    /**
     * Searches for users with roles AGENT or ADMIN whose usernames contain the given query.
     * @param query substring to match against usernames
     * @return a list of {@link UserResponse} DTOs for matching agent and admin users
     */
    public List<UserResponse> searchAgentAndAdminUsers(String query) {
        List<User> users = userRepository.findUsersByUsernameContainsIgnoreCaseAndRoleIsNot(query, Role.USER);

        return users.stream()
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

    /**
     * Searches for all users whose usernames contain the given query.
     * @param query substring to match against usernames
     * @return a list of {@link UserResponse} DTOs for matching users
     */
    public List<UserResponse> searchAllUsers(String query) {
        List<User> users = userRepository.findUsersByUsernameContainsIgnoreCase(query);

        return users.stream()
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


    /**
     * Registers a new user account.
     * @param request the {@link UserRequest} containing username, password, and email
     * @return a {@link UserResponse} representing the newly created user,
     *         including assigned ID, username, email, role, and submitted ticket IDs
     * @throws ResponseStatusException with HTTP 409 (CONFLICT) if username or email is already in use
     */
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
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

    /**
     * Retrieves all users in the system.
     * @return a list of {@link UserResponse} DTOs for every existing user
     */
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

    /**
     * Retrieves all tickets submitted by the specified user.
     * @param userId the unique identifier of the user
     * @return a list of {@link TicketResponse} DTOs for tickets authored by the user
     * @throws ResponseStatusException with HTTP 404 (NOT_FOUND) if no user exists with the given ID
     */
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
