package com.radioblog.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class BlogSubscription {
    @EmbeddedId
    private BlogSubscriptionId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("blogId")
    private Blog blog;

    private long lastReadPostId;

    public BlogSubscriptionId getId() {
        return id;
    }

    public BlogSubscription setId(BlogSubscriptionId id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public BlogSubscription setUser(User user) {
        this.user = user;
        return this;
    }

    public Blog getBlog() {
        return blog;
    }

    public BlogSubscription setBlog(Blog blog) {
        this.blog = blog;
        return this;
    }

    public long getLastReadPostId() {
        return lastReadPostId;
    }

    public BlogSubscription setLastReadPostId(long lastReadPostId) {
        this.lastReadPostId = lastReadPostId;
        return this;
    }

    @Override
    public String toString() {
        return "BlogSubscription{" +
                "id=" + id +
                ", user=" + user +
                ", blog=" + blog +
                ", lastReadPostId=" + lastReadPostId +
                '}';
    }
}
