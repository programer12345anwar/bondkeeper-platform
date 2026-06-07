package com.bondkeeper.backend.service;

import com.bondkeeper.backend.dto.request.LoginRequest;
import com.bondkeeper.backend.dto.request.RefreshTokenRequest;
import com.bondkeeper.backend.dto.request.UserRequest;
import com.bondkeeper.backend.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(UserRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout();
}
