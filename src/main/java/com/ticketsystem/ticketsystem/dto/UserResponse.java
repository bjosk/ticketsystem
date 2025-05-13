package com.ticketsystem.ticketsystem.dto;

import java.util.List;

public record UserResponse(
        Long userId,
        String username,
        String email,
        String role,
        List<Long> submittedTicketIds
) {}
