package com.blog.mappers;

import com.blog.dtos.commentdto.CommentRequestDTO;
import com.blog.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toCommentEntity(CommentRequestDTO requestDTO);

    void toUpdateComment(CommentRequestDTO requestDTO, @MappingTarget Comment comment);
}
