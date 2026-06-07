package com.bondkeeper.backend.service.impl;

import com.bondkeeper.backend.dto.request.UpdateProfileRequest;
import com.bondkeeper.backend.dto.response.UserResponse;
import com.bondkeeper.backend.entity.User;
import com.bondkeeper.backend.exception.DuplicateResourceException;
import com.bondkeeper.backend.exception.ResourceNotFoundException;
import com.bondkeeper.backend.mapper.EntityMapper;
import com.bondkeeper.backend.repository.UserRepository;
import com.bondkeeper.backend.service.UserService;
import com.bondkeeper.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getProfile() {
        return entityMapper.toUserResponse(findCurrentUser());
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = findCurrentUser();

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Email already in use: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return entityMapper.toUserResponse(userRepository.save(user));
    }

    private User findCurrentUser() {
        return userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
