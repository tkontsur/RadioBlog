package com.radioblog.service;

import com.radioblog.dto.BlogPostDTO;
import com.radioblog.entity.Blog;
import com.radioblog.entity.BlogPost;
import com.radioblog.entity.User;
import com.radioblog.repository.BlogPostsRepository;
import com.radioblog.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlogPostsServiceImplTest {
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private BlogPostsRepository blogPostsRepository;
    @Mock
    AwsCommunicationService awsCommunicationService;
    @InjectMocks
    private BlogPostsServiceImpl blogPostsService;
    private Blog blog;

    @BeforeEach
    public void setUp() {
        User user = new User()
                .setUsername("test")
                .setRole(User.Role.AUTHOR);
        blog = new Blog().setTitle("test").setOwner(user).setId(1L);
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        when(blogPostsRepository.save(ArgumentMatchers.any(BlogPost.class)))
                .thenAnswer(invocation -> ((BlogPost)invocation.getArgument(0)).setId(1L));
    }

    @Test
    public void createPost() {
        BlogPostDTO blogPostDTO = new BlogPostDTO(0, "test", "test", null, blog.getId());
        BlogPostDTO createdPost = blogPostsService.upsertPost(blogPostDTO);

        assertNotNull(createdPost);
        assertEquals(blogPostDTO.title(), createdPost.title());
        assertEquals(blogPostDTO.content(), createdPost.content());
        assertEquals(blog.getId(), createdPost.blogId());
        verify(awsCommunicationService).enqueueMp3FileGeneration(createdPost.id());
    }

    @Test
    public void editPost() {
        when(blogPostsRepository.findById(1L))
                .thenReturn(Optional.of(new BlogPost().setTitle("test").setContent("test").setBlog(blog).setId(1L)));

        BlogPostDTO editedPost = new BlogPostDTO(1L, "test edited", "test edited", null, blog.getId());
        BlogPostDTO updatedPost = blogPostsService.upsertPost(editedPost);

        assertEquals("test edited", updatedPost.title());
        assertEquals("test edited", updatedPost.content());
    }
}