package com.blog.domain.repositories;

import com.blog.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    @Query(value = """
            SELECT comment FROM Comment comment JOIN FETCH comment.user user
            WHERE comment.commentId = :commentId AND user.userId = :userId
            """)
    Optional<Comment> findByIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);
}
