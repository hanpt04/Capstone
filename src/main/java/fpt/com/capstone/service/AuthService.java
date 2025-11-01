package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.ChangePasswordRequest;
import fpt.com.capstone.dto.request.LoginRequest;
import fpt.com.capstone.dto.request.RefreshTokenRequest;
import fpt.com.capstone.dto.response.AuthResponse;
import fpt.com.capstone.dto.response.MessageResponse;
import fpt.com.capstone.dto.response.UserProfileResponse;
import fpt.com.capstone.exception.CustomException;
import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.model.Mentor;
import fpt.com.capstone.repository.LecturerRepository;
import fpt.com.capstone.repository.MentorRepository;
import fpt.com.capstone.security.CustomUserDetailsService;
import fpt.com.capstone.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final MentorRepository mentorRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    // Login
    public AuthResponse login(LoginRequest request) {
        // Find account first to check existence
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Email hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED));

        if (!account.isStatus()) {
            throw new CustomException("Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN);
        }

        // Authenticate with UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(account.getId()));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        String.valueOf(account.getId()),
                        request.getPassword(),
                        userDetails.getAuthorities()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate tokens
        String accessToken = jwtUtils.generateAccessToken(account);
        String refreshToken = jwtUtils.generateRefreshToken(account);

        // Build response
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getExpirationTime())
                .user(buildUserProfile(account))
                .build();
    }

    // Refresh Token
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtils.validateToken(refreshToken)) {
            throw new CustomException("Refresh token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED);
        }

        Integer userId = jwtUtils.getUserIdFromToken(refreshToken);
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Người dùng không tồn tại", HttpStatus.NOT_FOUND));

        if (!account.isStatus()) {
            throw new CustomException("Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN);
        }

        // Generate new tokens
        String newAccessToken = jwtUtils.generateAccessToken(account);
        String newRefreshToken = jwtUtils.generateRefreshToken(account);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getExpirationTime())
                .user(buildUserProfile(account))
                .build();
    }

    // Get Current User Profile
    public UserProfileResponse getCurrentUserProfile() {
        Account account = getCurrentUser();
        return buildUserProfile(account);
    }

    // Change Password
    public MessageResponse changePassword(ChangePasswordRequest request) {
        Account account = getCurrentUser();

        // Validate old password
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new CustomException("Mật khẩu cũ không đúng", HttpStatus.BAD_REQUEST);
        }

        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new CustomException("Mật khẩu mới và xác nhận mật khẩu không khớp", HttpStatus.BAD_REQUEST);
        }

        // Check if new password is same as old password
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new CustomException("Mật khẩu mới phải khác mật khẩu cũ", HttpStatus.BAD_REQUEST);
        }

        // Update password
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);

        return new MessageResponse("Đổi mật khẩu thành công");
    }

    // Helper: Get Current User
    private Account getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return accountRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new CustomException("Người dùng không tồn tại", HttpStatus.NOT_FOUND));
    }

    // Helper: Build User Profile Response
    private UserProfileResponse buildUserProfile(Account account) {
        UserProfileResponse.UserProfileResponseBuilder builder = UserProfileResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .fullName(account.getFullName())
                .phoneNumber(account.getPhoneNumber())
                .role(account.getRole().name())
                .status(account.isStatus())
                .createdAt(account.getCreatedAt());

        // Add role-specific information
        switch (account.getRole()) {
            case STUDENT:
                Student student = studentRepository.findById(account.getId())
                        .orElseThrow(() -> new CustomException("Sinh viên không tồn tại", HttpStatus.NOT_FOUND));
                builder.code(student.getStudentCode())
                        .gpa(student.getGpa());

                // Add mentor info if exists
                if (student.getMentor() != null) {
                    Mentor mentor = student.getMentor();
                    builder.mentor(UserProfileResponse.MentorInfo.builder()
                            .id(mentor.getId())
                            .fullName(mentor.getFullName())
                            .email(mentor.getEmail())
                            .mentorCode(mentor.getMentorCode())
                            .build());
                }
                break;

            case LECTURER:
                Lecturer lecturer = lecturerRepository.findById(account.getId())
                        .orElseThrow(() -> new CustomException("Giảng viên không tồn tại", HttpStatus.NOT_FOUND));
                builder.code(lecturer.getLecturerCode());
                break;

            case MENTOR:
                Mentor mentor = mentorRepository.findById(account.getId())
                        .orElseThrow(() -> new CustomException("Mentor không tồn tại", HttpStatus.NOT_FOUND));
                builder.code(mentor.getMentorCode());
                break;

            case ADMIN:
                // Admin doesn't have additional code
                break;
        }

        return builder.build();
    }
}
