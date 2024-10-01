package com.blog.api.mappers;

import com.blog.api.dtos.postdto.PostRequestDTO;
import com.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPostEntity(PostRequestDTO requestDTO);

    void toUpdatePost(PostRequestDTO requestDTO, @MappingTarget Post post);
}
