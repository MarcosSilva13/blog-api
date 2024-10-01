package com.blog.domain.specifications;

import com.blog.domain.entities.Comment;
import com.blog.domain.entities.Post;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class CommentSpecs {
    private CommentSpecs() {}

    public static Specification<Comment> listByPostId(Long postId) {
        return (root, query, builder) -> {
            root.fetch("user", JoinType.INNER);

            if (ObjectUtils.isEmpty(postId)) {
                return builder.disjunction();
            }

            Join<Comment, Post> postJoin = root.join("post");
            return builder.equal(postJoin.get("postId"), postId);
        };
    }
}
