package com.radioblog.service;

import com.radioblog.dto.BlogPostDTO;
import com.radioblog.entity.Blog;
import com.radioblog.entity.BlogPost;
import com.radioblog.repository.BlogPostsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogPostsServiceImpl implements BlogPostsService {
    private final BlogPostsRepository blogPostsRepository;
    private final AwsCommunicationService awsCommunicationService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(BlogPostsServiceImpl.class);

    @Autowired
    public BlogPostsServiceImpl(BlogPostsRepository blogPostsRepository,
                                AwsCommunicationService awsCommunicationService, UserService userService) {
        this.blogPostsRepository = blogPostsRepository;
        this.awsCommunicationService = awsCommunicationService;
        this.userService = userService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BlogPostDTO upsertPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = saveBlogPost(blogPostDTO);
        logger.debug("Saved blog post: {}. Starting mp3 generation.", blogPost);
        triggerAsyncMp3Generation(blogPost.getId());

        return new BlogPostDTO(blogPost.getId(), blogPost.getTitle(), blogPost.getContent(), blogPost.getMp3Url(),
                userService.getCurrentUser().getBlog().getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void triggerAsyncMp3Generation(long blogPostId) {
        awsCommunicationService.enqueueMp3FileGeneration(blogPostId);
    }

    @Transactional
    public BlogPost saveBlogPost(BlogPostDTO blogPostDTO) {
        Blog blog = userService.getCurrentUser().getBlog();

        if (blog == null) {
            throw new IllegalArgumentException("User does not have a blog");
        }

        if (blogPostDTO.blogId() > 0 && blogPostDTO.blogId() != blog.getId()) {
            throw new IllegalArgumentException("Blog ID does not match user's blog ID");
        }

        BlogPost blogPost = blogPostDTO.id() > 0
                ? blogPostsRepository.findById(blogPostDTO.id()).orElseThrow(EntityNotFoundException::new)
                : new BlogPost().setBlog(blog);

        blogPost.setTitle(blogPostDTO.title())
                .setContent(blogPostDTO.content());

        return blogPostsRepository.save(blogPost);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPostDTO> getPostsByBlogId(long blogId) {
        return blogPostsRepository.findAllByBlogId(blogId).stream()
                .map(blogPost -> new BlogPostDTO(blogPost.getId(), blogPost.getTitle(), blogPost.getContent(),
                        blogPost.getMp3Url(), blogPost.getBlog().getId()))
                .collect(Collectors.toList());
    }
}
