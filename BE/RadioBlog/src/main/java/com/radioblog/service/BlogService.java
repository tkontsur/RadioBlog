package com.radioblog.service;

import com.radioblog.dto.BlogDTO;

public interface BlogService {
    BlogDTO upsertBlog(BlogDTO blogDTO);
    BlogDTO getBlog(long id);
}
