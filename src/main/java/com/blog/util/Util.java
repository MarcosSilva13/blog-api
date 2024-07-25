package com.blog.util;

import com.blog.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class Util {

    private Util() {
        throw new IllegalStateException("Classe de utilidades.");
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
