package com.ticketsystem.ticketsystem.User;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsUsersById(Long id);
    List<User> findUsersByUsernameContainsIgnoreCaseAndRoleIsNot(String username, Role role);
}
