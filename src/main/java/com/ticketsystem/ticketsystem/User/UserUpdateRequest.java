package com.ticketsystem.ticketsystem.User;

public record UserUpdateRequest(
        String selectedUsername,
        String newUsername,
        String email,
        String role
) {}
