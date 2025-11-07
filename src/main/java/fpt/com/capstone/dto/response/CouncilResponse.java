package fpt.com.capstone.dto.response;

import fpt.com.capstone.model.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouncilResponse {

    // Thông tin từ Council
    private int id;
    private String name;
    private String description;
    private int status;
    private Semester semester; // Giữ nguyên object Semester

    // Thông tin "join" thủ công
    private List<CouncilMemberResponse> members; // <-- Dùng inner class

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouncilMemberResponse {
        private int memberId;
        private String role;
        private int lecturerId;
        private String lecturerName;
        private String lecturerEmail;
    }
}