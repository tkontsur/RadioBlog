package com.radioblog.service;

import com.radioblog.BaseSpringTest;
import com.radioblog.entity.Blog;
import com.radioblog.entity.BlogPost;
import com.radioblog.entity.User;
import com.radioblog.repository.BlogPostsRepository;
import com.radioblog.repository.BlogRepository;
import com.radioblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.polly.model.StartSpeechSynthesisTaskResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class AwsCommunicationServiceImplTest extends BaseSpringTest {
    @Autowired
    private AwsCommunicationService awsCommunicationService;
    @Autowired
    private BlogPostsRepository blogPostsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    private Blog blog;

    @BeforeEach
    public void setUp() {
        User user = new User()
                .setUsername("test")
                .setRole(User.Role.USER);
        userRepository.save(user);

        blog = new Blog().setTitle("Test Blog").setOwner(user);
        blogRepository.save(blog);
    }

    @Test
    void enqueueMp3FileGeneration() throws ExecutionException, InterruptedException {
        BlogPost post = new BlogPost()
                .setTitle("Test Title")
                .setContent("Test Content")
                .setBlog(blog);
        blogPostsRepository.save(post);

        CompletableFuture<StartSpeechSynthesisTaskResponse> future =
                awsCommunicationService.enqueueMp3FileGeneration(post.getId());
        StartSpeechSynthesisTaskResponse result = future.get();
        assertNotNull(result);
        System.out.println("Request sent: " + result);

        post = blogPostsRepository.findById(post.getId()).orElseThrow();
        assertNotNull(post.getMp3Url());
        assertEquals(post.getMp3Url(), result.synthesisTask().outputUri());
    }
}