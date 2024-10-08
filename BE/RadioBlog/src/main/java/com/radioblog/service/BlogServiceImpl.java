package com.radioblog.service;

import com.radioblog.dto.BlogDTO;
import com.radioblog.entity.Blog;
import com.radioblog.entity.User;
import com.radioblog.repository.BlogRepository;
import com.radioblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BlogDTO upsertBlog(BlogDTO blogDTO) {
        logger.debug("Creating blog with title: {}", blogDTO.title());
        User user = userRepository.findById(blogDTO.ownerId()).orElseThrow(EntityNotFoundException::new);

        if (user.getBlog() != null) {
            if (blogDTO.id() != user.getBlog().getId()) {
                throw new IllegalArgumentException("Blog ID does not match user's blog ID");
            }

            logger.debug("Updating existing blog with title: {}", blogDTO.title());
            user.getBlog().setTitle(blogDTO.title());
        } else {
            logger.debug("Creating new blog with title: {}", blogDTO.title());
            user.setBlog(new Blog(blogDTO.title(), user));
        }

        user.setRole(User.Role.AUTHOR);
        logger.debug("Updating user role to AUTHOR: {}", user);
        user = userRepository.save(user);
        Blog blog = user.getBlog();
        return new BlogDTO(blog.getId(), blog.getTitle(), user.getId(), user.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public BlogDTO getBlog(long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new BlogDTO(blog.getId(), blog.getTitle(), blog.getOwner().getId(), blog.getOwner().getUsername());
    }

    @Override
    @Transactional
    public List<BlogDTO> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(blog -> new BlogDTO(blog.getId(), blog.getTitle(), blog.getOwner().getId(), blog.getOwner().getUsername()))
                .toList();
    }
}
