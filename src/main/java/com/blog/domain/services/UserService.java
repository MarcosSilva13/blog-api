package com.blog.domain.services;

import com.blog.api.dtos.userdto.PasswordDTO;
import com.blog.api.dtos.userdto.UpdateUserRequestDTO;
import com.blog.api.dtos.userdto.UserResponseDTO;
import com.blog.domain.entities.User;
import com.blog.infra.exceptions.EmailUsedException;
import com.blog.infra.exceptions.MismatchedPasswordException;
import com.blog.api.mappers.UserMapper;
import com.blog.domain.repositories.UserRepository;
import com.blog.domain.util.Util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponseDTO getLoggedUser() {
        User user = Util.getCurrentUser();
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(UpdateUserRequestDTO requestDTO) {
        User user = Util.getCurrentUser();

        if (!user.getEmail().equals(requestDTO.email())) {
            verifyEmailInUse(requestDTO.email());
        }

        userMapper.toUpdateUser(requestDTO, user);
        return new UserResponseDTO(userRepository.save(user));
    }

    private void verifyEmailInUse(String emailToUpdate) {
        if (userRepository.existsByEmail(emailToUpdate)) {
            throw new EmailUsedException("O email já está em uso.");
        }
    }

    @Transactional
    public void updateUserPassword(PasswordDTO passwordDTO) {
        User user = Util.getCurrentUser();

        if (!passwordEncoder.matches(passwordDTO.oldPassword(), user.getPassword())) {
            throw new MismatchedPasswordException("Senha antiga incompatível.");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser() {
        User user = Util.getCurrentUser();
        userRepository.delete(user);
    }
}