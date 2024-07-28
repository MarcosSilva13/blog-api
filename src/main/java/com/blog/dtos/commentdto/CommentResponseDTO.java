package com.blog.dtos.commentdto;

import com.blog.dtos.userdto.UserBasicResponseDTO;
import com.blog.entities.Comment;

import java.time.LocalDateTime;

public record CommentResponseDTO(Long id, String content, LocalDateTime createdAt, LocalDateTime updatedAt,
                                 UserBasicResponseDTO user) {

    public CommentResponseDTO(Comment comment) {
        this(comment.getCommentId(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt(),
                new UserBasicResponseDTO(comment.getUser()));
    }
}
