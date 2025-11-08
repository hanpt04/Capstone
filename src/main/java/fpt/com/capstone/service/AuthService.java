package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.ChangePasswordRequest;
import fpt.com.capstone.dto.request.LoginRequest;
import fpt.com.capstone.dto.request.RefreshTokenRequest;
import fpt.com.capstone.dto.response.AuthResponse;
import fpt.com.capstone.dto.response.MessageResponse;
import fpt.com.capstone.dto.response.UserProfileResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    UserProfileResponse getCurrentUserProfile();
    MessageResponse changePassword(ChangePasswordRequest request);
}
