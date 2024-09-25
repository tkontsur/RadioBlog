package com.radioblog.entity;

import com.radioblog.dto.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(length = 50, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<BlogSubscription> subscriptions = new ArrayList<>();

    public User() {
    }

    public User(UserDTO dto) {
        this.id = dto.id();
        this.username = dto.username();
        this.role = Role.valueOf(dto.role());
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String email) {
        this.username = email;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public List<BlogSubscription> getSubscriptions() {
        return subscriptions;
    }

    public User setSubscriptions(List<BlogSubscription> subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }

    public void addSubscription(BlogSubscription subscription) {
        this.subscriptions.add(subscription);
    }

    public void removeSubscription(BlogSubscription subscription) {
        this.subscriptions.remove(subscription);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", subscriptions=" + subscriptions +
                '}';
    }

    public enum Role {
        ADMIN, BLOGGER, USER
    }
}
