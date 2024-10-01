package com.blog.domain.services;

import com.blog.api.dtos.authdto.LoginRequestDTO;
import com.blog.api.dtos.authdto.RegisterRequestDTO;
import com.blog.api.dtos.authdto.TokenDTO;
import com.blog.api.dtos.userdto.UserResponseDTO;
import com.blog.domain.entities.Role;
import com.blog.domain.entities.User;
import com.blog.infra.exceptions.EmailUsedException;
import com.blog.api.mappers.UserMapper;
import com.blog.domain.repositories.UserRepository;
import com.blog.infra.security.TokenService;
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
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       TokenService tokenService, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponseDTO register(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.email())) {
            throw new EmailUsedException("O email já está em uso.");
        }

        User user = userMapper.toUserEntity(requestDTO);
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
