package com.blog.api.dtos.postdto;

import com.blog.api.dtos.userdto.UserBasicResponseDTO;
import com.blog.domain.entities.Post;

import java.time.LocalDateTime;

public record PostResponseDTO(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt,
                              UserBasicResponseDTO user) {

    public PostResponseDTO(Post post) {
        this(post.getPostId(), post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUpdatedAt(),
                new UserBasicResponseDTO(post.getUser()));
    }
}
