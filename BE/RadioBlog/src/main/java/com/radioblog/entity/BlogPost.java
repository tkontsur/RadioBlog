package com.radioblog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String mp3Url;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    public Long getId() {
        return id;
    }

    public BlogPost setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BlogPost setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public BlogPost setContent(String content) {
        this.content = content;
        return this;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public BlogPost setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
        return this;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public BlogPost setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public Blog getBlog() {
        return blog;
    }

    public BlogPost setBlog(Blog blog) {
        this.blog = blog;
        return this;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", mp3Url='" + mp3Url + '\'' +
                ", publishedAt=" + publishedAt +
                ", blog=" + blog +
                '}';
    }
}
