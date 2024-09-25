package com.radioblog.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class BlogSubscriptionId implements Serializable {
    private long userId;
    private long blogId;

    public long getUserId() {
        return userId;
    }

    public BlogSubscriptionId setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getBlogId() {
        return blogId;
    }

    public BlogSubscriptionId setBlogId(long blogId) {
        this.blogId = blogId;
        return this;
    }

    @Override
    public String toString() {
        return "BlogSubscriptionId{" +
                "userId=" + userId +
                ", blogId=" + blogId +
                '}';
    }
}