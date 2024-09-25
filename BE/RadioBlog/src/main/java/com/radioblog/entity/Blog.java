package com.radioblog.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @OneToOne
    @JoinColumn(nullable = false)
    private User owner;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BlogPost> posts = new ArrayList<>();

    @OneToMany(mappedBy = "blog")
    private List<BlogSubscription> subscriptions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Blog setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Blog setTitle(String title) {
        this.title = title;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public Blog setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public List<BlogPost> getPosts() {
        return posts;
    }

    public Blog setPosts(List<BlogPost> posts) {
        this.posts = posts;
        return this;
    }

    public List<BlogSubscription> getSubscriptions() {
        return subscriptions;
    }

    public Blog setSubscriptions(List<BlogSubscription> subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", owner=" + owner +
                ", posts=" + posts +
                ", subscriptions=" + subscriptions +
                '}';
    }
}
