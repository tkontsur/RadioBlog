package com.radioblog.service;

import com.radioblog.dto.BlogDTO;

public interface BlogService {
    BlogDTO addBlog(BlogDTO blogDTO);
    BlogDTO getBlog(long id);
}
