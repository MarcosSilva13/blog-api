package com.blog.dtos.userdto;

import com.blog.entities.User;

public record UserBasicResponseDTO(Long id, String name, String profileImage) {

    public UserBasicResponseDTO(User user) {
        this(user.getUserId(), user.getName(), user.getProfileImage());
    }
}
