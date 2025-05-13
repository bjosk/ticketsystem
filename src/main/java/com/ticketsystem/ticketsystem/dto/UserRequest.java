package com.ticketsystem.ticketsystem.dto;

import java.util.List;

public record UserRequest(
        String username,
        String password,
        String email
) {}
