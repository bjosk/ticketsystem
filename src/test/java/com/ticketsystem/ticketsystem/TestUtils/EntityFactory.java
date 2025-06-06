package com.ticketsystem.ticketsystem.TestUtils;

import com.ticketsystem.ticketsystem.User.Role;
import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityFactory {

    /**
     * Creates a User with the given id, username, email, role, and optionally a list of ticket IDs.
     */
    public static User createUserWithTickets(long userId,
                                             String username,
                                             String email,
                                             Role role,
                                             long... ticketIds) throws ReflectiveOperationException
    {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);

        // Reflectively set user.id = userId
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);

        // If ticketIds are provided, create Ticket objects and assign to u.submittedTickets
        if (ticketIds != null && ticketIds.length > 0) {
            List<Ticket> tickets = new ArrayList<>();
            for (long ticketId : ticketIds) {
                tickets.add(createTicket(ticketId, user));
            }
            user.setSubmittedTickets(tickets);
        }

        return user;
    }

    /**
     * Creates a Ticket with the given ticketId and submittedBy user.
     * Other fields (shortDescription, description, ticketStatus) can be minimal or default.
     */
    public static Ticket createTicket(long ticketId, User submittedBy) throws ReflectiveOperationException {
        Ticket ticket = new Ticket();
        ticket.setShortDescription("dummy");
        ticket.setDescription("dummy");
        ticket.setTicketStatus(TicketStatus.NEW);
        ticket.setSubmittedBy(submittedBy);

        // Reflectively set ticket.ticketId = ticketId
        Field ticketIdField = Ticket.class.getDeclaredField("ticketId");
        ticketIdField.setAccessible(true);
        ticketIdField.set(ticket, ticketId);

        return ticket;
    }
}
