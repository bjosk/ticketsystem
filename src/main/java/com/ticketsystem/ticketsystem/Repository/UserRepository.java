package com.ticketsystem.ticketsystem.Repository;

import com.ticketsystem.ticketsystem.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsUsersById(Long id);
}
