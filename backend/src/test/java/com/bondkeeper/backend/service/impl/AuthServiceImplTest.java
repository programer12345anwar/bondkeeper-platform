package com.bondkeeper.backend.service.impl;

import com.bondkeeper.backend.dto.request.LoginRequest;
import com.bondkeeper.backend.dto.request.UserRequest;
import com.bondkeeper.backend.dto.response.AuthResponse;
import com.bondkeeper.backend.dto.response.UserResponse;
import com.bondkeeper.backend.entity.User;
import com.bondkeeper.backend.entity.enums.UserRole;
import com.bondkeeper.backend.exception.DuplicateResourceException;
import com.bondkeeper.backend.mapper.EntityMapper;
import com.bondkeeper.backend.repository.RefreshTokenRepository;
import com.bondkeeper.backend.repository.UserRepository;
import com.bondkeeper.backend.security.JwtService;
import com.bondkeeper.backend.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private EntityMapper entityMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_duplicateEmail_throwsException() {
        UserRequest request = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@bondkeeper.app")
                .password("password123")
                .build();

        when(userRepository.existsByEmail("john@bondkeeper.app")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.register(request));
    }

    @Test
    void register_success_returnsAuthResponse() {
        UserRequest request = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@bondkeeper.app")
                .password("password123")
                .build();

        User user = new User();
        user.setId(1L);
        user.setEmail("john@bondkeeper.app");
        user.setRole(UserRole.ROLE_USER);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(entityMapper.toUser(request)).thenReturn(new User());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshTokenValue()).thenReturn("refresh-token");
        when(jwtService.getAccessTokenExpirationMs()).thenReturn(900_000L);
        when(jwtService.getRefreshTokenExpirationMs()).thenReturn(604_800_000L);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(entityMapper.toUserResponse(user)).thenReturn(UserResponse.builder().id(1L).email("john@bondkeeper.app").build());

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void login_success_returnsAuthResponse() {
        LoginRequest request = LoginRequest.builder()
                .email("demo@bondkeeper.app")
                .password("password")
                .build();

        User user = new User();
        user.setId(1L);
        user.setEmail("demo@bondkeeper.app");
        user.setRole(UserRole.ROLE_USER);
        UserPrincipal principal = new UserPrincipal(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
        when(jwtService.generateAccessToken(principal)).thenReturn("access-token");
        when(jwtService.generateRefreshTokenValue()).thenReturn("refresh-token");
        when(jwtService.getAccessTokenExpirationMs()).thenReturn(900_000L);
        when(jwtService.getRefreshTokenExpirationMs()).thenReturn(604_800_000L);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(entityMapper.toUserResponse(user)).thenReturn(UserResponse.builder().id(1L).build());

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        verify(refreshTokenRepository).revokeAllByUserId(1L);
    }
}
