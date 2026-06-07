package com.bondkeeper.backend.service.impl;

import com.bondkeeper.backend.dto.request.LoginRequest;
import com.bondkeeper.backend.dto.request.RefreshTokenRequest;
import com.bondkeeper.backend.dto.request.UserRequest;
import com.bondkeeper.backend.dto.response.AuthResponse;
import com.bondkeeper.backend.dto.response.UserResponse;
import com.bondkeeper.backend.entity.RefreshToken;
import com.bondkeeper.backend.entity.User;
import com.bondkeeper.backend.entity.enums.UserRole;
import com.bondkeeper.backend.exception.BusinessException;
import com.bondkeeper.backend.exception.DuplicateResourceException;
import com.bondkeeper.backend.mapper.EntityMapper;
import com.bondkeeper.backend.repository.RefreshTokenRepository;
import com.bondkeeper.backend.repository.UserRepository;
import com.bondkeeper.backend.security.JwtService;
import com.bondkeeper.backend.security.UserPrincipal;
import com.bondkeeper.backend.service.AuthService;
import com.bondkeeper.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        User user = entityMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        User saved = userRepository.save(user);
        return buildAuthResponse(new UserPrincipal(saved));
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        refreshTokenRepository.revokeAllByUserId(principal.getId());
        return buildAuthResponse(principal);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken stored = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException("Invalid refresh token"));

        if (stored.getRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("Refresh token expired or revoked");
        }

        User user = stored.getUser();
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        return buildAuthResponse(new UserPrincipal(user));
    }

    @Override
    @Transactional
    public void logout() {
        refreshTokenRepository.revokeAllByUserId(SecurityUtils.getCurrentUserId());
    }

    private AuthResponse buildAuthResponse(UserPrincipal principal) {
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshTokenValue = jwtService.generateRefreshTokenValue();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(userRepository.getReferenceById(principal.getId()))
                .expiresAt(Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        UserResponse userResponse = entityMapper.toUserResponse(
                userRepository.findById(principal.getId()).orElseThrow());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationMs() / 1000)
                .user(userResponse)
                .role(principal.getRole())
                .build();
    }
}
