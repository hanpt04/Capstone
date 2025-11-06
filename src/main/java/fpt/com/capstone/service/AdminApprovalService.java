package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.AdminReviewRequest;
import fpt.com.capstone.dto.response.AdminReviewResponse;
import fpt.com.capstone.dto.response.ProposalAdminStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminApprovalService {
    // Admin review (first-come-first-serve)
    AdminReviewResponse reviewProposal(AdminReviewRequest request, Integer adminId);

    // Get proposals
    Page<ProposalAdminStatusResponse> getProposalsForAdminReview(String status, Pageable pageable);
    ProposalAdminStatusResponse getProposalAdminStatus(Integer proposalId);

    // Helper
    boolean hasAdminReviewed(Integer adminId, Integer proposalId);
    void checkAndUpdateProposalStatus(Integer proposalId);
}
