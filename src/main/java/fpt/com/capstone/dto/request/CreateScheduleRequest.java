package fpt.com.capstone.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CreateScheduleRequest {

    @NotNull(message = "ID Đồ án không được để trống")
    private Integer capstoneProjectId;

    @NotNull(message = "ID Hội đồng không được để trống")
    private Integer councilId;

    @NotNull(message = "Ngày bảo vệ không được để trống")
    @FutureOrPresent(message = "Ngày bảo vệ phải là hôm nay hoặc trong tương lai")
    private LocalDate defenseDate;

    @NotNull(message = "Giờ bắt đầu không được để trống")
    private LocalTime startTime;

    @NotNull(message = "Giờ kết thúc không được để trống")
    private LocalTime endTime;

    @NotBlank(message = "Phòng không được để trống")
    private String room;
}