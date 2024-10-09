package com.radioblog.controller;

import com.radioblog.dto.BlogDTO;
import com.radioblog.dto.BlogPostDTO;
import com.radioblog.service.BlogPostsService;
import com.radioblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/blogs")
public class BlogsController {
    private final BlogService blogService;
    private final BlogPostsService blogPostsService;

    @Autowired
    public BlogsController(BlogService blogService, BlogPostsService blogPostsService) {
        this.blogService = blogService;
        this.blogPostsService = blogPostsService;
    }

    @GetMapping
    public List<BlogDTO> getBlogs() {
        return blogService.getAllBlogs();
    }

    @PostMapping
    public BlogDTO createBlog(@RequestBody BlogDTO blogDTO) {
        return blogService.upsertBlog(blogDTO);
    }

    @GetMapping("/{blogId}/posts")
    public List<BlogPostDTO> getPostsByBlogId(@PathVariable long blogId) {
        return blogPostsService.getPostsByBlogId(blogId);
    }

    @PostMapping("/post")
    public void addPost(@RequestBody BlogPostDTO blogPostDTO) {
        blogPostsService.upsertPost(blogPostDTO);
    }
}
