package com.blog.domain.utils;

import com.blog.domain.entities.Role;
import com.blog.domain.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestUtils {

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User createDefaultUser() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Marcos");
        user.setEmail("marcos@gmail.com");
        user.setPassword(passwordEncoder.encode("123"));
        user.setRole(new Role(2));
        return user;
    }

}
