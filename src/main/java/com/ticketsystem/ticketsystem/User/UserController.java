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

    //Searches for agent and admin users
    @GetMapping("/searchAgentAndAdmin")
    public List<UserResponse> searchAgentAndAdminUsers(@RequestParam String usernameQuery) {
        return userService.searchAgentAndAdminUsers(usernameQuery);
    }

    //Searches for regular users
    @GetMapping("/searchAllUsers")
    public List<UserResponse> searchAllUsers(@RequestParam String usernameQuery) {
        return userService.searchAllUsers(usernameQuery);
    }

    // Create a new user (register)
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    // Get all users
    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    // Get tickets by authorId
    @GetMapping("/{userId}/tickets")
    public List<TicketResponse> getTicketsByUserId(@PathVariable Long userId) {
        return userService.getTicketsByUserId(userId);
    }

}
