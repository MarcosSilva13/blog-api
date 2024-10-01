package com.blog.api.filters;

import com.blog.domain.entities.Comment;
import com.blog.domain.specifications.CommentSpecs;
import org.springframework.data.jpa.domain.Specification;

public record CommentQueryFilter(Long postId) {

    public Specification<Comment> toSpecification() {
        return CommentSpecs.listByPostId(postId);
    }
}
