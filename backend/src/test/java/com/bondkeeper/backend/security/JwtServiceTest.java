package com.bondkeeper.backend.security;

import com.bondkeeper.backend.config.JwtProperties;
import com.bondkeeper.backend.entity.User;
import com.bondkeeper.backend.entity.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        properties.setAccessTokenExpirationMs(900_000);
        properties.setRefreshTokenExpirationMs(604_800_000);
        properties.setIssuer("bondkeeper-test");
        jwtService = new JwtService(properties);
    }

    @Test
    void generateAndValidateAccessToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@bondkeeper.app");
        user.setPassword("encoded");
        user.setRole(UserRole.ROLE_USER);

        UserPrincipal principal = new UserPrincipal(user);
        String token = jwtService.generateAccessToken(principal);

        assertNotNull(token);
        assertEquals("test@bondkeeper.app", jwtService.extractUsername(token));
        assertEquals(1L, jwtService.extractUserId(token));
        assertTrue(jwtService.isTokenValid(token, principal));
    }

    @Test
    void generateRefreshTokenValue() {
        String refreshToken = jwtService.generateRefreshTokenValue();
        assertNotNull(refreshToken);
    }
}
