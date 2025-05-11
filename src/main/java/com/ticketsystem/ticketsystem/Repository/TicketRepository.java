package com.ticketsystem.ticketsystem.Repository;

import com.ticketsystem.ticketsystem.Model.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
}