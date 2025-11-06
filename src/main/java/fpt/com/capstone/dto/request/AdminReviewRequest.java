package fpt.com.capstone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminReviewRequest {

    @NotNull(message = "Proposal ID is required")
    private Integer proposalId;

    @NotNull(message = "Approval decision is required")
    private Boolean isApproved; // true = approve, false = reject

    @NotBlank(message = "Comments are required")
    @Size(min = 10, max = 1000, message = "Comments must be between 10 and 1000 characters")
    private String comments;
}
