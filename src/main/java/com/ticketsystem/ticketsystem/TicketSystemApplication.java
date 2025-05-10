package com.ticketsystem.ticketsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketSystemApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

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
    }
}
