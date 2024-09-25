package com.radioblog.service;

import com.radioblog.dto.BlogPostDTO;

import java.util.List;

public interface BlogPostsService {
    BlogPostDTO upsertPost(BlogPostDTO blogPostDTO);

    List<BlogPostDTO> getPostsByBlogId(long blogId);
}
