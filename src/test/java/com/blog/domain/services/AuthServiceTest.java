package com.blog.domain.services;

import com.blog.api.dtos.authdto.LoginRequestDTO;
import com.blog.api.dtos.authdto.RegisterRequestDTO;
import com.blog.api.dtos.authdto.TokenDTO;
import com.blog.api.dtos.userdto.UserResponseDTO;
import com.blog.api.mappers.UserMapper;
import com.blog.domain.entities.User;
import com.blog.domain.repositories.UserRepository;
import com.blog.domain.utils.TestUtils;
import com.blog.infra.exceptions.EmailUsedException;
import com.blog.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserMapper userMapper;

    private User user;

    private RegisterRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        user = TestUtils.createDefaultUser();
        requestDTO = new RegisterRequestDTO("Marcos", "marcos@gmail.com", "123");
    }

    @Test
    @DisplayName("register should create user when successful")
    void registerShouldCreateUserWhenSuccessful() {
        UserResponseDTO expectedResponseDTO = new UserResponseDTO(user);

        when(userRepository.existsByEmail(requestDTO.email())).thenReturn(false);
        when(userMapper.toUserEntity(requestDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO userResponseDTO = authService.register(requestDTO);

        assertThat(userResponseDTO).isNotNull().isEqualTo(expectedResponseDTO);

        verify(userRepository).existsByEmail(requestDTO.email());
        verify(userMapper).toUserEntity(requestDTO);
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("register should throw EmailUsedException when user email is duplicated")
    void registerShouldThrowEmailUsedExceptionWhenUserEmailIsDuplicated() {
        when(userRepository.existsByEmail(requestDTO.email())).thenReturn(true);

        assertThatExceptionOfType(EmailUsedException.class)
                .isThrownBy(() -> authService.register(requestDTO))
                .withMessage("O email já está em uso.");

        verify(userRepository).existsByEmail(requestDTO.email());
        verifyNoInteractions(userMapper, bCryptPasswordEncoder);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("login should return token when successful")
    void loginShouldReturnTokenWhenSuccessful() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("marcos@gmail.com", "123");
        String expectedToken = "mockToken123";
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(user)).thenReturn(expectedToken);

        TokenDTO tokenDTO = authService.login(loginRequestDTO);

        assertThat(tokenDTO.token()).isNotNull().isEqualTo(expectedToken);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(user);
        verifyNoMoreInteractions(authenticationManager, tokenService);
    }
}

