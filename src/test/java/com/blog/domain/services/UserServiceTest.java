package com.blog.domain.services;

import com.blog.api.dtos.userdto.PasswordDTO;
import com.blog.api.dtos.userdto.UpdateUserRequestDTO;
import com.blog.api.dtos.userdto.UserResponseDTO;
import com.blog.api.mappers.UserMapper;
import com.blog.domain.entities.Role;
import com.blog.domain.entities.User;
import com.blog.domain.repositories.UserRepository;
import com.blog.domain.utils.TestUtils;
import com.blog.infra.exceptions.EmailUsedException;
import com.blog.infra.exceptions.MismatchedPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestUtils.createDefaultUser();
    }

    private void getAuthentication() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("getLoggedUser should return the user logged")
    void getLoggedUserShouldReturnTheUserLogged() {
        getAuthentication();
        UserResponseDTO expectedResponseDTO = new UserResponseDTO(user);
        UserResponseDTO userResponseDTO = userService.getLoggedUser();

        assertThat(userResponseDTO).isNotNull().isEqualTo(expectedResponseDTO);
    }

    @Test
    @DisplayName("updateUser should update user data when successful")
    void updateUserShouldUpdateUserDataWhenSuccessful() {
        getAuthentication();
        User userUpdated = new User();
        userUpdated.setUserId(1L);
        userUpdated.setName("Marcos alterado");
        userUpdated.setEmail("marcos13@gmail.com");
        userUpdated.setRole(new Role(2));

        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO("Marcos alterado", "marcos13@gmail.com", null);
        UserResponseDTO expectedResponseDTO = new UserResponseDTO(userUpdated);

        when(userRepository.existsByEmail(requestDTO.email())).thenReturn(false);
        doNothing().when(userMapper).toUpdateUser(requestDTO, user);
        when(userRepository.save(any(User.class))).thenReturn(userUpdated);

        UserResponseDTO userResponseDTO = userService.updateUser(requestDTO);

        assertThat(userResponseDTO).isNotNull().isEqualTo(expectedResponseDTO);

        verify(userRepository).existsByEmail(requestDTO.email());
        verify(userMapper).toUpdateUser(requestDTO, user);
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("updateUser should throw EmailUsedException when user email is duplicated")
    void updateUserShouldThrowEmailUsedExceptionWhenUserEmailIsDuplicated() {
        getAuthentication();
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO("Marcos alterado", "marcos13@gmail.com", null);

        when(userRepository.existsByEmail(requestDTO.email())).thenReturn(true);

        assertThatExceptionOfType(EmailUsedException.class)
                .isThrownBy(() -> userService.updateUser(requestDTO))
                .withMessage("O email já está em uso.");

        verify(userRepository).existsByEmail(requestDTO.email());
        verifyNoInteractions(userMapper);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("updateUserPassword should update the password of user when successful")
    void updateUserPasswordShouldUpdateThePasswordOfUserWhenSuccessful() {
        getAuthentication();
        PasswordDTO passwordDTO = new PasswordDTO("123", "12345");
        String userPassword = user.getPassword();

        when(passwordEncoder.matches(passwordDTO.oldPassword(), userPassword)).thenReturn(true);
        when(passwordEncoder.encode(passwordDTO.newPassword())).thenReturn("12345");

        assertThatCode(() -> userService.updateUserPassword(passwordDTO))
                .doesNotThrowAnyException();

        verify(passwordEncoder).matches(passwordDTO.oldPassword(), userPassword);
        verify(passwordEncoder).encode(passwordDTO.newPassword());
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }

    @Test
    @DisplayName("updateUserPassword should throw MismatchedPasswordException when old password dont match encrypted password")
    void updateUserPasswordShouldThrowMismatchedPasswordExceptionWhenOldPasswordDontMatchEncryptedPassword() {
        getAuthentication();
        PasswordDTO passwordDTO = new PasswordDTO("1111", "12345");
        String userPassword = user.getPassword();

        when(passwordEncoder.matches(passwordDTO.oldPassword(), userPassword)).thenReturn(false);

        assertThatExceptionOfType(MismatchedPasswordException.class)
                .isThrownBy(() -> userService.updateUserPassword(passwordDTO))
                .withMessage("Senha antiga incompatível.");

        verify(passwordEncoder).matches(passwordDTO.oldPassword(), userPassword);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("deleteUser should delete user when successful")
    void deleteUserShouldDeleteUserWhenSuccessful() {
        getAuthentication();
        doNothing().when(userRepository).delete(user);

        assertThatCode(() -> userService.deleteUser()).doesNotThrowAnyException();

        verify(userRepository).delete(user);
        verifyNoMoreInteractions(userRepository);
    }
}
