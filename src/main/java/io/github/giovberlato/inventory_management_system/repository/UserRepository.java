package io.github.giovberlato.inventory_management_system.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import io.github.giovberlato.inventory_management_system.model.User;

public interface UserRepository extends ListCrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
