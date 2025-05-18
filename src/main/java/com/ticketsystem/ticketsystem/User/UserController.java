package com.ticketsystem.ticketsystem.User;

import com.ticketsystem.ticketsystem.Ticket.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


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

//    @GetMapping("/me")
//    public UserResponse getCurrentUser(Authentication auth) {
//        return userService.loadOrCreateUser(auth);
//    }


}
