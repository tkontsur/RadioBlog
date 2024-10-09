package com.radioblog.service;

import com.radioblog.dto.BlogDTO;

import java.util.List;

public interface BlogService {
    BlogDTO upsertBlog(BlogDTO blogDTO);
    BlogDTO getBlog(long id);

    List<BlogDTO> getAllBlogs();
}
