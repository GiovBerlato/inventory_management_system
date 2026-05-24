package io.github.giovberlato.inventory_management_system.model;

import java.util.UUID;

import io.github.giovberlato.inventory_management_system.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Role role;

    public User() {
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
