package com.blog.dtos.userdto;

import com.blog.entities.User;

public record UserBasicResponseDTO(Long id, String name) {

    public UserBasicResponseDTO(User user) {
        this(user.getUserId(), user.getName());
    }
}
