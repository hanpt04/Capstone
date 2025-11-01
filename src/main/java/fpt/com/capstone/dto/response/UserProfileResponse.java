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
    private String lecturerCode;
    private LocalDateTime createdAt;

}
