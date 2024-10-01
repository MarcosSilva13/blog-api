package com.blog.domain.repositories;

import com.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT user FROM User user JOIN FETCH user.role role
            WHERE user.email = :email
            """)
    Optional<UserDetails> findByEmail(@Param("email") String email);

    @Query(value = """
            SELECT (COUNT(user.email) > 0) FROM User user where user.email = :email
            """)
    boolean existsByEmail(@Param("email") String email);
}
