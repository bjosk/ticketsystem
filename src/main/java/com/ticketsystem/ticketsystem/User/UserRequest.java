package com.ticketsystem.ticketsystem.User;

public record UserRequest(
        String username,
        String password,
        String email
) {}
