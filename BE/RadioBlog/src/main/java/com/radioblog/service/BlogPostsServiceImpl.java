package com.radioblog.service;

import com.radioblog.dto.BlogPostDTO;
import com.radioblog.entity.Blog;
import com.radioblog.entity.BlogPost;
import com.radioblog.repository.BlogPostsRepository;
import com.radioblog.repository.BlogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogPostsServiceImpl implements BlogPostsService {
    private final BlogPostsRepository blogPostsRepository;
    private final BlogRepository blogRepository;
    private final AwsCommunicationService awsCommunicationService;

    @Autowired
    public BlogPostsServiceImpl(BlogPostsRepository blogPostsRepository, BlogRepository blogRepository,
                                AwsCommunicationService awsCommunicationService) {
        this.blogPostsRepository = blogPostsRepository;
        this.blogRepository = blogRepository;
        this.awsCommunicationService = awsCommunicationService;
    }

    @Override
    @Transactional
    public BlogPostDTO upsertPost(BlogPostDTO blogPostDTO) {
        Blog blog = blogRepository.findById(blogPostDTO.blogId()).orElseThrow(EntityNotFoundException::new);

        BlogPost blogPost = blogPostDTO.id() > 0
                ? blogPostsRepository.findById(blogPostDTO.id()).orElseThrow(EntityNotFoundException::new)
                : new BlogPost().setBlog(blog);

        blogPost.setTitle(blogPostDTO.title())
                .setContent(blogPostDTO.content());

        blogPost = blogPostsRepository.save(blogPost);
        awsCommunicationService.enqueueMp3FileGeneration(blogPost.getId());

        return new BlogPostDTO(blogPost.getId(), blogPost.getTitle(), blogPost.getContent(), blogPost.getMp3Url(),
                blog.getId());
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
