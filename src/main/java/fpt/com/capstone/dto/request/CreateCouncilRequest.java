package fpt.com.capstone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateCouncilRequest {

    @NotBlank(message = "Tên hội đồng không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Học kỳ không được để trống")
    private Integer semesterId; // ID của học kỳ

    @NotEmpty(message = "Hội đồng phải có ít nhất một thành viên")
    private List<CouncilMemberRequest> members; // Sử dụng inner class bên dưới

    /**
     * Inner class (DTO phụ)
     * Định nghĩa thông tin của một thành viên trong hội đồng.
     */
    @Data
    @NoArgsConstructor
    public static class CouncilMemberRequest {

        @NotNull(message = "ID giảng viên không được để trống")
        private Integer lecturerId; // ID của giảng viên

        @NotBlank(message = "Vai trò không được để trống")
        private String role; // "PRESIDENT", "SECRETARY", "REVIEWER", "GUEST"
    }
}