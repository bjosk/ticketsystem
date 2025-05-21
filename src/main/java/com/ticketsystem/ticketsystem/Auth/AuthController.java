package com.ticketsystem.ticketsystem.Auth;

import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.User.UserRepository;
import com.ticketsystem.ticketsystem.User.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
