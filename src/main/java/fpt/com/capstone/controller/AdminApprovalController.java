package fpt.com.capstone.controller;

import fpt.com.capstone.dto.request.AdminReviewRequest;
import fpt.com.capstone.dto.response.AdminReviewResponse;
import fpt.com.capstone.dto.response.ProposalAdminStatusResponse;
import fpt.com.capstone.service.AdminApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin-approvals")
@RequiredArgsConstructor
public class AdminApprovalController {

    private final AdminApprovalService adminApprovalService;

    /**
     * Admin reviews (approve/reject) a proposal
     * POST /api/admin-approvals/review
     *
     * First-come-first-serve: Admin nào vào trước sẽ là Admin1
     */
    @PostMapping("/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> reviewProposal(
            @Valid @RequestBody AdminReviewRequest request,
            Authentication authentication) {

        Integer adminId = Integer.valueOf(authentication.getName());

        AdminReviewResponse review = adminApprovalService.reviewProposal(request, adminId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", review);
        response.put("message", "Review submitted successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Get all proposals for admin review
     * GET /api/admin-approvals/proposals?status=pending
     *
     * status=pending: Proposals in DUPLICATE_ACCEPTED (waiting for review)
     * status=reviewed: Proposals that completed admin review
     */
    @GetMapping("/proposals")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getProposalsForAdminReview(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<ProposalAdminStatusResponse> proposals = adminApprovalService
                .getProposalsForAdminReview(status, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of(
                "content", proposals.getContent(),
                "totalPages", proposals.getTotalPages(),
                "totalElements", proposals.getTotalElements(),
                "currentPage", proposals.getNumber()
        ));

        return ResponseEntity.ok(response);
    }

    /**
     * Get admin approval status for a specific proposal
     * GET /api/admin-approvals/proposal/{proposalId}/status
     */
    @GetMapping("/proposal/{proposalId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR')")
    public ResponseEntity<Map<String, Object>> getProposalAdminStatus(
            @PathVariable Integer proposalId) {

        ProposalAdminStatusResponse statusResponse = adminApprovalService
                .getProposalAdminStatus(proposalId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", statusResponse);

        return ResponseEntity.ok(response);
    }
}
