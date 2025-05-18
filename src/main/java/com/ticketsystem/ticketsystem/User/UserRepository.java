package com.ticketsystem.ticketsystem.User;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsUsersById(Long id);

    Optional<User> findByExternalId(String externalId);
}
