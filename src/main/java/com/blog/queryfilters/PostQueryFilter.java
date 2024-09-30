package com.blog.queryfilters;

import com.blog.entities.Post;
import com.blog.specifications.PostSpecs;
import org.springframework.data.jpa.domain.Specification;

public record PostQueryFilter(String title) {

    public Specification<Post> toSpecification() {
        return PostSpecs.titleContains(title);
    }
}
