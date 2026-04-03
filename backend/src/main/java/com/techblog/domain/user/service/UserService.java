package com.techblog.domain.user.service;

import com.techblog.domain.user.dto.ChangePasswordRequest;
import com.techblog.domain.user.dto.UpdateProfileRequest;
import com.techblog.domain.user.dto.UserProfileResponse;

public interface UserService {
    UserProfileResponse getMyProfile(String email);
    UserProfileResponse updateMyProfile(String email, UpdateProfileRequest request);
    void changePassword(String email, ChangePasswordRequest request);
}