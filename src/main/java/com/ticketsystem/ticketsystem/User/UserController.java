package com.ticketsystem.ticketsystem.User;

import com.ticketsystem.ticketsystem.Ticket.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Updates an existing user’s details.
     * @param userUpdateRequest the {@link UserUpdateRequest} containing fields to modify
     */
    @PutMapping
    public void updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
    }

    /**
     * Searches for users with roles AGENT or ADMIN whose usernames match the query.
     * @param usernameQuery substring to match against usernames
     * @return a list of {@link UserResponse} DTOs for matching agent and admin users
     */
    @GetMapping("/searchAgentAndAdmin")
    public List<UserResponse> searchAgentAndAdminUsers(@RequestParam String usernameQuery) {
        return userService.searchAgentAndAdminUsers(usernameQuery);
    }


    /**
     * Searches for all users (regardless of role) whose usernames match the query.
     * @param usernameQuery substring to match against usernames
     * @return a list of {@link UserResponse} DTOs for matching users
     */
    @GetMapping("/searchAllUsers")
    public List<UserResponse> searchAllUsers(@RequestParam String usernameQuery) {
        return userService.searchAllUsers(usernameQuery);
    }

    /**
     * Registers a new user account.
     * @param request the {@link UserRequest} DTO containing the new user’s details
     * @return a {@link UserResponse} representing the newly created user
     */
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    /**
     * Retrieves all users in the system.
     * @return a list of {@link UserResponse} DTOs for every existing user
     */
    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Retrieves all tickets submitted by the specified user.
     * @param userId the unique identifier of the user whose tickets are requested
     * @return a list of {@link TicketResponse} DTOs for tickets authored by the user
     */
    @GetMapping("/{userId}/tickets")
    public List<TicketResponse> getTicketsByUserId(@PathVariable Long userId) {
        return userService.getTicketsByUserId(userId);
    }

}
