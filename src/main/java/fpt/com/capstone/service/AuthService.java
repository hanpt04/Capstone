package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.ChangePasswordRequest;
import fpt.com.capstone.dto.request.LoginRequest;
import fpt.com.capstone.dto.request.RefreshTokenRequest;
import fpt.com.capstone.dto.response.AuthResponse;
import fpt.com.capstone.dto.response.MessageResponse;
import fpt.com.capstone.dto.response.UserProfileResponse;
import fpt.com.capstone.exception.CustomException;
import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.repository.LecturerRepository;
import fpt.com.capstone.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final LecturerRepository lecturerRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // Login
    public AuthResponse login(LoginRequest request) {
        // Authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Find lecturer
        Lecturer lecturer = lecturerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Email hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED));

        if (!lecturer.isStatus()) {
            throw new CustomException("Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN);
        }

        return createAuthResponse(lecturer);
    }

    // Refresh Token
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtils.validateToken(refreshToken)) {
            throw new CustomException("Refresh token không hợp lệ", HttpStatus.UNAUTHORIZED);
        }

        Integer userId = jwtUtils.getUserIdFromToken(refreshToken);
        Lecturer lecturer = lecturerRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User không tồn tại", HttpStatus.NOT_FOUND));

        if (!lecturer.isStatus()) {
            throw new CustomException("Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN);
        }

        return createAuthResponse(lecturer);
    }

    // Get Current User Profile
    public UserProfileResponse getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Lecturer lecturer = lecturerRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new CustomException("User không tồn tại", HttpStatus.NOT_FOUND));

        return buildUserProfile(lecturer);
    }

    // Change Password
    @Transactional
    public MessageResponse changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new CustomException("Mật khẩu mới và xác nhận không khớp", HttpStatus.BAD_REQUEST);
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new CustomException("Mật khẩu mới phải khác mật khẩu cũ", HttpStatus.BAD_REQUEST);
        }

        Lecturer lecturer = lecturerRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new CustomException("User không tồn tại", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), lecturer.getPassword())) {
            throw new CustomException("Mật khẩu cũ không đúng", HttpStatus.BAD_REQUEST);
        }

        lecturer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        lecturerRepository.save(lecturer);

        return new MessageResponse("Đổi mật khẩu thành công");
    }

    // Helper methods
    private AuthResponse createAuthResponse(Lecturer lecturer) {
        String accessToken = jwtUtils.generateAccessToken(lecturer);
        String refreshToken = jwtUtils.generateRefreshToken(lecturer);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getExpirationTime())
                .user(buildUserProfile(lecturer))
                .build();
    }

    private UserProfileResponse buildUserProfile(Lecturer lecturer) {
        return UserProfileResponse.builder()
                .id(lecturer.getId())
                .email(lecturer.getEmail())
                .fullName(lecturer.getFullName())
                .phoneNumber(lecturer.getPhoneNumber())
                .role(lecturer.getRole().name())
                .status(lecturer.isStatus())
                .lecturerCode(lecturer.getLecturerCode())
                .createdAt(lecturer.getCreatedAt())
                .build();
    }
}
