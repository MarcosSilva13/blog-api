package com.blog.domain.services;

import com.blog.api.dtos.commentdto.CommentRequestDTO;
import com.blog.api.dtos.commentdto.CommentResponseDTO;
import com.blog.domain.entities.Comment;
import com.blog.domain.entities.Post;
import com.blog.domain.entities.User;
import com.blog.api.mappers.CommentMapper;
import com.blog.api.filters.CommentQueryFilter;
import com.blog.domain.repositories.CommentRepository;
import com.blog.domain.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comentário não encontrado.";

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentResponseDTO> getAll(CommentQueryFilter commentQueryFilter) {
        return commentRepository.findAll(commentQueryFilter.toSpecification())
                .stream().map(CommentResponseDTO::new).toList();
    }

    @Transactional
    public CommentResponseDTO createComment(Long postId, CommentRequestDTO requestDTO) {
        Comment comment = commentMapper.toCommentEntity(requestDTO);
        User user = Util.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();

        comment.setUser(user);
        comment.setPost(new Post(postId));
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);

        return new CommentResponseDTO(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO requestDTO) {
        Long userId = Util.getCurrentUser().getUserId();
        Comment comment = getByIdAndUserId(commentId, userId);

        commentMapper.toUpdateComment(requestDTO, comment);
        comment.setUpdatedAt(LocalDateTime.now());

        return new CommentResponseDTO(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        User user = Util.getCurrentUser();
        Long userId = user.getUserId();
        String userRole = user.getRole().getName();

        Comment comment = switch (userRole) {
            case "ADMIN" -> commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE));
            case "USER" -> getByIdAndUserId(commentId, userId);
            default -> throw new IllegalStateException("Valor inesperado: " + userRole);
        };

        commentRepository.delete(comment);
    }

    private Comment getByIdAndUserId(Long commentId, Long userId) {
        return commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE));
    }
}
