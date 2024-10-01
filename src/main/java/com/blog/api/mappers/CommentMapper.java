package com.blog.api.mappers;

import com.blog.api.dtos.commentdto.CommentRequestDTO;
import com.blog.domain.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toCommentEntity(CommentRequestDTO requestDTO);

    void toUpdateComment(CommentRequestDTO requestDTO, @MappingTarget Comment comment);
}
