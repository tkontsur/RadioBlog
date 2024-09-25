package com.radioblog.repository;

import com.radioblog.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostsRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findAllByBlogId(long blogId);
}
