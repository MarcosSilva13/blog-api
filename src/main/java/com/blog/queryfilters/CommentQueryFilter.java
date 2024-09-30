package com.blog.queryfilters;

import com.blog.entities.Comment;
import com.blog.specifications.CommentSpecs;
import org.springframework.data.jpa.domain.Specification;

public record CommentQueryFilter(Long postId) {

    public Specification<Comment> toSpecification() {
        return CommentSpecs.listByPostId(postId);
    }
}
