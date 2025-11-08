package fpt.com.capstone.dto.request;

import fpt.com.capstone.model.DefenseResult;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateDefenseResultRequest {

    @NotNull(message = "ID của Lịch (Schedule) không được để trống")
    private Integer scheduleId;

    @NotBlank(message = "Trạng thái (PASS/FAIL) không được để trống")
    private DefenseResult.DefenseStatus status;

    @NotNull(message = "Điểm số không được để trống")
    @Min(value = 0, message = "Điểm số không được âm")
    private Double score;

    private String comments;
}