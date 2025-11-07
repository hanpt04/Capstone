package fpt.com.capstone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateCouncilRequest {

    @NotBlank(message = "Tên hội đồng không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Học kỳ không được để trống")
    private Integer semesterId;

    @NotEmpty(message = "Hội đồng phải có ít nhất một thành viên")
    private List<CreateCouncilRequest.CouncilMemberRequest> members; // Tái sử dụng DTO con
}