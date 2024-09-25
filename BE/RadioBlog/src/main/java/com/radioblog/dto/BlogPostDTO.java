package com.radioblog.dto;

public record BlogPostDTO(long id, String title, String content, String mp3Url, long blogId) {
}
