package com.blog.repositories;

import com.blog.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
            SELECT post FROM Post post JOIN FETCH post.user user
            """)
    Page<Post> findAll(Pageable pageable);
}
