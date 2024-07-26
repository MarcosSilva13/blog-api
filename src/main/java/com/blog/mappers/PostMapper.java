package com.blog.mappers;

import com.blog.dtos.postdto.PostRequestDTO;
import com.blog.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPostEntity(PostRequestDTO requestDTO);

    void toUpdatePost(PostRequestDTO requestDTO, @MappingTarget Post post);
}
