package fpt.com.capstone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalAdminStatusResponse {
    private Integer proposalId;
    private String proposalTitle;
    private String currentStatus;

    // Admin 1 status
    private Boolean isAdmin1Approved; // null = chưa review
    private String admin1Comments;
    private String admin1Name;
    private String admin1ReviewedAt;

    // Admin 2 status
    private Boolean isAdmin2Approved; // null = chưa review
    private String admin2Comments;
    private String admin2Name;
    private String admin2ReviewedAt;

    // Overall
    private String finalDecision; // "PENDING", "APPROVED", "REJECTED"
    private Integer totalReviewsCompleted; // 0, 1, or 2
}
