package fpt.com.capstone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {
    private Integer id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String role;
    private Boolean status;
    private String code; // studentCode, lecturerCode, mentorCode
    private Double gpa; // for Student only
    private LocalDateTime createdAt;

    // For Student with Mentor info
    private MentorInfo mentor;

    @Data
    @Builder
    public static class MentorInfo {
        private Integer id;
        private String fullName;
        private String email;
        private String mentorCode;
    }
}
