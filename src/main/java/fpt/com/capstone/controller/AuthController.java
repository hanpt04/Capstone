//package fpt.com.capstone.controller;
//
//import fpt.com.capstone.dto.request.ChangePasswordRequest;
//import fpt.com.capstone.dto.request.LoginRequest;
//import fpt.com.capstone.dto.request.RefreshTokenRequest;
//import fpt.com.capstone.dto.response.AuthResponse;
//import fpt.com.capstone.dto.response.MessageResponse;
//import fpt.com.capstone.dto.response.UserProfileResponse;
//import fpt.com.capstone.service.AuthService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@Validated
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    /**
//     * POST /api/auth/login
//     * Đăng nhập cho Admin, Mentor, Lecturer (KHÔNG có Student)
//     */
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
//        AuthResponse response = authService.login(request);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * POST /api/auth/logout
//     * Đăng xuất
//     */
//    @PostMapping("/logout")
//    public ResponseEntity<MessageResponse> logout() {
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công"));
//    }
//
//    /**
//     * POST /api/auth/refresh-token
//     * Refresh access token
//     */
//    @PostMapping("/refresh-token")
//    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
//        AuthResponse response = authService.refreshToken(request);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * GET /api/auth/profile
//     * Lấy thông tin profile người dùng hiện tại
//     */
//    @GetMapping("/profile")
//    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
//        UserProfileResponse response = authService.getCurrentUserProfile();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * PUT /api/auth/change-password
//     * Đổi mật khẩu
//     */
//    @PutMapping("/change-password")
//    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
//        MessageResponse response = authService.changePassword(request);
//        return ResponseEntity.ok(response);
//    }
//}
