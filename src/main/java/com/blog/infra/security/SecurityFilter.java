package com.blog.infra.security;

import com.blog.api.dtos.ErrorDTO;
import com.blog.domain.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);

        try {
            if (token != null) {
                String email = tokenService.validateToken(token);
                authenticateUser(email);
            }
        } catch (RuntimeException exception) {
            ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.name(), HttpStatus.UNAUTHORIZED.value(),
                    exception.getMessage(), request.getRequestURI(), LocalDateTime.now().toString());

            ObjectMapper objectMapper = new ObjectMapper();

            response.setStatus(errorDTO.status());
            response.setContentType("application/json");
            response.getWriter().println(objectMapper.writeValueAsString(errorDTO));
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String email) {
        Optional<UserDetails> userDetails = userRepository.findByEmail(email);

        if (userDetails.isPresent()) {
            var authentication = new UsernamePasswordAuthenticationToken(userDetails.get(), null,
                    userDetails.get().getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        return header != null ? header.replace("Bearer ", "") : null;
    }
}
