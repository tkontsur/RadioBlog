package com.radioblog.service;

import com.radioblog.BaseSpringTest;
import com.radioblog.dto.BlogDTO;
import com.radioblog.entity.User;
import com.radioblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class BlogServiceImplTest extends BaseSpringTest {
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User()
                .setUsername("test")
                .setRole(User.Role.USER);
        userRepository.save(user);
    }

    @Test
    public void createBlog() {
        BlogDTO blogDTO = new BlogDTO(0, "test", user.getId());
        BlogDTO createdBlog = blogService.upsertBlog(blogDTO);

        assertNotNull(createdBlog);
        assertEquals(blogDTO.title(), createdBlog.title());
        assertEquals(user.getId(), createdBlog.ownerId());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(User.Role.AUTHOR, updatedUser.getRole());
        assertEquals(createdBlog.id(), updatedUser.getBlog().getId());

        BlogDTO fetchedBlog = blogService.getBlog(createdBlog.id());
        assertEquals(createdBlog, fetchedBlog);
    }

    @Test
    public void editBlog() {
        BlogDTO blogDTO = new BlogDTO(0, "test", user.getId());
        BlogDTO createdBlog = blogService.upsertBlog(blogDTO);
        assertNotNull(createdBlog);

        BlogDTO editedBlog = new BlogDTO(createdBlog.id(), "edited", user.getId());
        BlogDTO updatedBlog = blogService.upsertBlog(editedBlog);
        assertEquals(updatedBlog.id(), createdBlog.id());
        assertEquals(updatedBlog.title(),
                userRepository.findById(user.getId()).orElseThrow().getBlog().getTitle());
    }

    @Test
    public void createBlogWithInvalidOwnerId() {
        BlogDTO blogDTO = new BlogDTO(0, "test", user.getId());
        blogService.upsertBlog(blogDTO);

        assertThrows(IllegalArgumentException.class,
                () -> blogService.upsertBlog(new BlogDTO(0, blogDTO.title(), blogDTO.ownerId())));
    }
}