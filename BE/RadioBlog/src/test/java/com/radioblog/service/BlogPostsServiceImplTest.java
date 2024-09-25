package com.radioblog.service;

import com.radioblog.BaseSpringTest;
import com.radioblog.dto.BlogPostDTO;
import com.radioblog.entity.Blog;
import com.radioblog.entity.User;
import com.radioblog.repository.BlogRepository;
import com.radioblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlogPostsServiceImplTest extends BaseSpringTest {
    @Autowired
    private BlogPostsService blogPostsService;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    private Blog blog;

    @BeforeEach
    public void setUp() {
        User user = new User()
                .setUsername("test")
                .setRole(User.Role.AUTHOR);
        userRepository.save(user);

        blog = new Blog().setTitle("test").setOwner(user);
        blogRepository.save(blog);
    }

    @Test
    public void createPost() {
        BlogPostDTO blogPostDTO = new BlogPostDTO(0, "test", "test", null, blog.getId());
        BlogPostDTO createdPost = blogPostsService.upsertPost(blogPostDTO);

        assertNotNull(createdPost);
        assertEquals(blogPostDTO.title(), createdPost.title());
        assertEquals(blogPostDTO.content(), createdPost.content());
        assertEquals(blog.getId(), createdPost.blogId());
    }

    @Test
    public void editPost() {
        BlogPostDTO blogPostDTO = new BlogPostDTO(0, "test", "test", null, blog.getId());
        BlogPostDTO createdPost = blogPostsService.upsertPost(blogPostDTO);
        assertNotNull(createdPost);

        BlogPostDTO editedPost = new BlogPostDTO(createdPost.id(), "edited", "edited", null, blog.getId());
        BlogPostDTO updatedPost = blogPostsService.upsertPost(editedPost);

        List<BlogPostDTO> posts = blogPostsService.getPostsByBlogId(blog.getId());
        assertEquals(1, posts.size());
        assertEquals(updatedPost, posts.getFirst());
    }

    @Test
    public void createPostWithInvalidBlogId() {
        BlogPostDTO blogPostDTO = new BlogPostDTO(0, "test", "test", null, blog.getId() + 1);
        assertThrows(EntityNotFoundException.class, () -> blogPostsService.upsertPost(blogPostDTO));
    }
}