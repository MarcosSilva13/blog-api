package com.blog.services;

import com.blog.dtos.authdto.LoginRequestDTO;
import com.blog.dtos.authdto.RegisterRequestDTO;
import com.blog.dtos.authdto.TokenDTO;
import com.blog.dtos.userdto.UserResponseDTO;
import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.exceptions.EmailUsedException;
import com.blog.repositories.UserRepository;
import com.blog.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       TokenService tokenService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public UserResponseDTO register(RegisterRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.email()).isPresent()) {
            throw new EmailUsedException("O email já está em uso.");
        }

        User user = new User();
        user.setName(requestDTO.name());
        user.setEmail(requestDTO.email());
        user.setPassword(bCryptPasswordEncoder.encode(requestDTO.password()));
        user.setRole(new Role(2));

        return new UserResponseDTO(userRepository.save(user));
    }

    public TokenDTO login(LoginRequestDTO requestDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(requestDTO.email(), requestDTO.password());

        var auth = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());

        return new TokenDTO(token);
    }
}
