package com.ticketsystem.ticketsystem.TestUtils;

import com.ticketsystem.ticketsystem.Comment.Comment;
import com.ticketsystem.ticketsystem.User.Role;
import com.ticketsystem.ticketsystem.User.User;
import com.ticketsystem.ticketsystem.Ticket.Ticket;
import com.ticketsystem.ticketsystem.Ticket.TicketStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityFactory {

    //Create a comment and assign it to a ticket
    public static Comment createComment (long commentId, User user, Ticket ticket) throws Exception {
        Comment comment = new Comment();
        comment.setText("Dummy comment");
        comment.setAuthor(user);
        comment.setTicket(ticket);

        Field commmentIdField = Comment.class.getDeclaredField("id");
        commmentIdField.setAccessible(true);
        commmentIdField.set(comment, commentId);
        return comment;
    }

    /**
     * Creates a list of fake users.
     * @param count how many users to generate
     * @return a List<User> of size count
     */
    public static List<User> createUsers(int count) {
        try {
            List<User> list = new ArrayList<>(count);
            Role[] roles = Role.values();
            for (int i = 1; i <= count; i++) {
                long id = i;
                String username = "user" + i;
                String email = "user" + i + "@example.com";
                Role role = roles[i % roles.length];

                // Build a long[] of ticket IDs
                long[] tickets = (i % 3 == 0)
                        ? new long[0]
                        : new long[]{ 100L + i };

                list.add(createUserWithTickets(id, username, email, role, tickets));
            }
            return list;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }



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
