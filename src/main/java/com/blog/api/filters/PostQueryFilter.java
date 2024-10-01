package com.blog.api.filters;

import com.blog.domain.entities.Post;
import com.blog.domain.specifications.PostSpecs;
import org.springframework.data.jpa.domain.Specification;

public record PostQueryFilter(String title) {

    public Specification<Post> toSpecification() {
        return PostSpecs.titleContains(title);
    }
}
