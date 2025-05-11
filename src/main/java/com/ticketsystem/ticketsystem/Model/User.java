package com.ticketsystem.ticketsystem.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "submittedBy")
    private List<Ticket> submittedTickets;

    public List<Ticket> getSubmittedTickets() {
        return submittedTickets;
    }

    public void setSubmittedTickets(List<Ticket> submittedTickets) {
        this.submittedTickets = submittedTickets;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
