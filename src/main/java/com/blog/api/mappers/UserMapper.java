package com.blog.api.mappers;

import com.blog.api.dtos.authdto.RegisterRequestDTO;
import com.blog.api.dtos.userdto.UpdateUserRequestDTO;
import com.blog.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "password", target = "password", ignore = true)
    User toUserEntity(RegisterRequestDTO requestDTO);

    void toUpdateUser(UpdateUserRequestDTO requestDTO, @MappingTarget User user);
}
