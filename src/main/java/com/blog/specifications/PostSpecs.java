package com.blog.specifications;

import com.blog.entities.Post;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class PostSpecs {

    private PostSpecs() {}

    public static Specification<Post> titleContains(String title) {
        return (root, query, builder) -> {
          root.fetch("user", JoinType.INNER);

          if (ObjectUtils.isEmpty(title)) {
              return builder.conjunction();
          }

          return builder.like(root.get("title"), "%" + title + "%");
        };
    }
}
