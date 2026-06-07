package com.bondkeeper.backend.service;

import com.bondkeeper.backend.dto.request.UpdateProfileRequest;
import com.bondkeeper.backend.dto.response.UserResponse;

public interface UserService {

    UserResponse getProfile();

    UserResponse updateProfile(UpdateProfileRequest request);
}
