package com.blog.domain.util;

import com.blog.domain.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class Util {

    private Util() {
        throw new IllegalStateException("Classe de utilidades.");
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
