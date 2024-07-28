package com.blog.dtos.userdto;

import com.blog.entities.User;

public record UserResponseDTO(Long id, String name, String email, String profileImage, Integer roleId) {

    public UserResponseDTO(User user) {
        this(user.getUserId(), user.getName(), user.getEmail(), user.getProfileImage(), user.getRole().getRoleId());
    }
}
