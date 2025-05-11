package com.ticketsystem.ticketsystem;

import com.ticketsystem.ticketsystem.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
