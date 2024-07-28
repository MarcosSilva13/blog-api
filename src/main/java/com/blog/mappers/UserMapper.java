package com.blog.mappers;

import com.blog.dtos.authdto.RegisterRequestDTO;
import com.blog.dtos.userdto.UpdateUserRequestDTO;
import com.blog.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "password", target = "password", ignore = true)
    User toUserEntity(RegisterRequestDTO requestDTO);

    void toUpdateUser(UpdateUserRequestDTO requestDTO, @MappingTarget User user);
}
