//package fpt.com.capstone.service;
//
//import fpt.com.capstone.dto.request.AdminReviewRequest;
//import fpt.com.capstone.dto.response.AdminReviewResponse;
//import fpt.com.capstone.dto.response.ProposalAdminStatusResponse;
//import fpt.com.capstone.exception.CustomException;
//import fpt.com.capstone.model.CapstoneProposal;
//import fpt.com.capstone.model.CapstoneProposalHistory;
//import fpt.com.capstone.model.Lecturer;
//import fpt.com.capstone.repository.CapstoneProposalHistoryRepository;
//import fpt.com.capstone.repository.CapstoneProposalRepository;
//import fpt.com.capstone.repository.LecturerRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@Transactional
//public class AdminApprovalServiceImpl implements AdminApprovalService {
//
//    private final CapstoneProposalRepository proposalRepository;
//    private final LecturerRepository lecturerRepository;
//    private final CapstoneProposalHistoryRepository historyRepository;
//    // TODO: EmailService
//
//    @Override
//    public AdminReviewResponse reviewProposal(AdminReviewRequest request, Integer adminId) {
//        log.info("Admin {} reviewing proposal {}", adminId, request.getProposalId());
//
//        // Validate proposal exists and in correct status
//        CapstoneProposal proposal = proposalRepository.findById(request.getProposalId())
//                .orElseThrow(() -> new CustomException(
//                        "Proposal not found with ID: " + request.getProposalId(),
//                        HttpStatus.NOT_FOUND
//                ));
//
//        // Must be in DUPLICATE_ACCEPTED status
//        if (proposal.getStatus() != CapstoneProposal.ProposalStatus.DUPLICATE_ACCEPTED) {
//            throw new CustomException(
//                    "Proposal must be in DUPLICATE_ACCEPTED status for admin review. Current status: " + proposal.getStatus(),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//
//        // Validate reviewer is ADMIN
//        Lecturer admin = lecturerRepository.findById(adminId)
//                .orElseThrow(() -> new CustomException(
//                        "Admin not found with ID: " + adminId,
//                        HttpStatus.NOT_FOUND
//                ));
//
//        if (admin.getRole() != Lecturer.AccountRole.ADMIN) {
//            throw new CustomException(
//                    "Only ADMINs can review proposals",
//                    HttpStatus.FORBIDDEN
//            );
//        }
//
//        // Check if this admin already reviewed
//        if (hasAdminReviewed(adminId, request.getProposalId())) {
//            throw new CustomException(
//                    "You have already reviewed this proposal",
//                    HttpStatus.CONFLICT
//            );
//        }
//
//        // Determine admin order (first-come-first-serve)
//        Integer adminOrder;
//        if (proposal.getIsAdmin1() == null) {
//            // Admin 1 slot available
//            adminOrder = 1;
//            proposal.setIsAdmin1(request.getIsApproved());
//            proposal.setAdmin1Comments(request.getComments());
//            proposal.setAdmin1Id(adminId);
//            proposal.setAdmin1ReviewedAt(LocalDateTime.now());
//        } else if (proposal.getIsAdmin2() == null) {
//            // Admin 2 slot available
//            adminOrder = 2;
//            proposal.setIsAdmin2(request.getIsApproved());
//            proposal.setAdmin2Comments(request.getComments());
//            proposal.setAdmin2Id(adminId);
//            proposal.setAdmin2ReviewedAt(LocalDateTime.now());
//        } else {
//            // Both slots taken
//            throw new CustomException(
//                    "Both admin reviews have been completed for this proposal",
//                    HttpStatus.CONFLICT
//            );
//        }
//
//        proposalRepository.save(proposal);
//        log.info("Admin {} reviewed proposal {} as Admin{}", adminId, request.getProposalId(), adminOrder);
//
//        // Check if both admins have reviewed and update status
//        checkAndUpdateProposalStatus(request.getProposalId());
//
//        // Return response
//        return AdminReviewResponse.builder()
//                .proposalId(proposal.getId())
//                .proposalTitle(proposal.getTitle())
//                .currentStatus(proposal.getStatus().name())
//                .adminOrder(adminOrder)
//                .isApproved(request.getIsApproved())
//                .comments(request.getComments())
//                .reviewerName(admin.getFullName())
//                .reviewedAt(LocalDateTime.now())
//                .build();
//    }
//
//    @Override
//    public void checkAndUpdateProposalStatus(Integer proposalId) {
//        log.info("Checking admin approval status for proposal {}", proposalId);
//
//        CapstoneProposal proposal = proposalRepository.findById(proposalId)
//                .orElseThrow(() -> new CustomException(
//                        "Proposal not found with ID: " + proposalId,
//                        HttpStatus.NOT_FOUND
//                ));
//
//        Boolean admin1Approved = proposal.getIsAdmin1();
//        Boolean admin2Approved = proposal.getIsAdmin2();
//
//        // Check if both admins have reviewed
//        if (admin1Approved != null && admin2Approved != null) {
//
//            if (Boolean.TRUE.equals(admin1Approved) && Boolean.TRUE.equals(admin2Approved)) {
//                // Both approved → Move to REVIEW_1
//                proposal.setStatus(CapstoneProposal.ProposalStatus.REVIEW_1);
//                proposalRepository.save(proposal);
//
//                log.info("Proposal {} approved by both admins. Status updated to REVIEW_1", proposalId);
//
//            } else {
//                // At least one rejected → REJECT_BY_ADMIN
//                proposal.setStatus(CapstoneProposal.ProposalStatus.REJECT_BY_ADMIN);
//                proposalRepository.save(proposal);
//
//                // Create history record
//                createRejectionHistory(proposal);
//
//                log.info("Proposal {} rejected by admin(s). Status updated to REJECT_BY_ADMIN", proposalId);
//            }
//        } else {
//            log.info("Proposal {} waiting for second admin review", proposalId);
//        }
//    }
//
//    @Override
//    public Page<ProposalAdminStatusResponse> getProposalsForAdminReview(String status, Pageable pageable) {
//        Page<CapstoneProposal> proposals;
//
//        if ("DUPLICATE_ACCEPTED".equalsIgnoreCase(status)) {
//            // Get proposals in DUPLICATE_ACCEPTED status (waiting for admin review)
//            proposals = proposalRepository.findByStatus(
//                    CapstoneProposal.ProposalStatus.DUPLICATE_ACCEPTED,
//                    pageable
//            );
//        } else {
//            // All proposals needing or completed admin review
//            proposals = proposalRepository.findByStatusIn(
//                    java.util.List.of(
//                            CapstoneProposal.ProposalStatus.DUPLICATE_ACCEPTED,
//                            CapstoneProposal.ProposalStatus.REVIEW_1,
//                            CapstoneProposal.ProposalStatus.REJECT_BY_ADMIN
//                    ),
//                    pageable
//            );
//        }
//
//        return proposals.map(this::mapToProposalAdminStatusResponse);
//    }
//
//    @Override
//    public ProposalAdminStatusResponse getProposalAdminStatus(Integer proposalId) {
//        CapstoneProposal proposal = proposalRepository.findById(proposalId)
//                .orElseThrow(() -> new CustomException(
//                        "Proposal not found with ID: " + proposalId,
//                        HttpStatus.NOT_FOUND
//                ));
//
//        return mapToProposalAdminStatusResponse(proposal);
//    }
//
//    @Override
//    public boolean hasAdminReviewed(Integer adminId, Integer proposalId) {
//        CapstoneProposal proposal = proposalRepository.findById(proposalId)
//                .orElseThrow(() -> new CustomException(
//                        "Proposal not found with ID: " + proposalId,
//                        HttpStatus.NOT_FOUND
//                ));
//
//        return adminId.equals(proposal.getAdmin1Id()) || adminId.equals(proposal.getAdmin2Id());
//    }
//
//    // Helper Methods
//
//    private ProposalAdminStatusResponse mapToProposalAdminStatusResponse(CapstoneProposal proposal) {
//        // Determine final decision
//        String finalDecision;
//        int completedReviews = 0;
//
//        if (proposal.getIsAdmin1() != null) completedReviews++;
//        if (proposal.getIsAdmin2() != null) completedReviews++;
//
//        if (completedReviews < 2) {
//            finalDecision = "PENDING";
//        } else if (Boolean.TRUE.equals(proposal.getIsAdmin1()) && Boolean.TRUE.equals(proposal.getIsAdmin2())) {
//            finalDecision = "APPROVED";
//        } else {
//            finalDecision = "REJECTED";
//        }
//
//        // Get admin names
//        String admin1Name = proposal.getAdmin1Id() != null ?
//                lecturerRepository.findById(proposal.getAdmin1Id())
//                        .map(Lecturer::getFullName)
//                        .orElse("Unknown") : null;
//
//        String admin2Name = proposal.getAdmin2Id() != null ?
//                lecturerRepository.findById(proposal.getAdmin2Id())
//                        .map(Lecturer::getFullName)
//                        .orElse("Unknown") : null;
//
//        return ProposalAdminStatusResponse.builder()
//                .proposalId(proposal.getId())
//                .proposalTitle(proposal.getTitle())
//                .currentStatus(proposal.getStatus().name())
//                .isAdmin1Approved(proposal.getIsAdmin1())
//                .admin1Comments(proposal.getAdmin1Comments())
//                .admin1Name(admin1Name)
//                .admin1ReviewedAt(proposal.getAdmin1ReviewedAt() != null ?
//                        proposal.getAdmin1ReviewedAt().toString() : null)
//                .isAdmin2Approved(proposal.getIsAdmin2())
//                .admin2Comments(proposal.getAdmin2Comments())
//                .admin2Name(admin2Name)
//                .admin2ReviewedAt(proposal.getAdmin2ReviewedAt() != null ?
//                        proposal.getAdmin2ReviewedAt().toString() : null)
//                .finalDecision(finalDecision)
//                .totalReviewsCompleted(completedReviews)
//                .build();
//    }
//
//    private void createRejectionHistory(CapstoneProposal proposal) {
//        StringBuilder reason = new StringBuilder("Proposal rejected by admin(s):\n\n");
//
//        if (Boolean.FALSE.equals(proposal.getIsAdmin1())) {
//            String admin1Name = proposal.getAdmin1Id() != null ?
//                    lecturerRepository.findById(proposal.getAdmin1Id())
//                            .map(Lecturer::getFullName)
//                            .orElse("Admin 1") : "Admin 1";
//
//            reason.append(String.format("❌ %s (Admin 1):\n%s\n\n",
//                    admin1Name,
//                    proposal.getAdmin1Comments()));
//        }
//
//        if (Boolean.FALSE.equals(proposal.getIsAdmin2())) {
//            String admin2Name = proposal.getAdmin2Id() != null ?
//                    lecturerRepository.findById(proposal.getAdmin2Id())
//                            .map(Lecturer::getFullName)
//                            .orElse("Admin 2") : "Admin 2";
//
//            reason.append(String.format("❌ %s (Admin 2):\n%s\n\n",
//                    admin2Name,
//                    proposal.getAdmin2Comments()));
//        }
//
//        CapstoneProposalHistory history = new CapstoneProposalHistory();
//        history.setCapstoneProposal(proposal);
//        history.setReason(reason.toString());
//        history.setTitle(proposal.getTitle());
//        history.setContext(proposal.getContext());
//        history.setDescription(proposal.getDescription());
//        history.setAttachmentUrl(proposal.getAttachmentUrl());
//        history.setFunc(proposal.getFunc() != null ? new ArrayList<>(proposal.getFunc()) : null );
//        history.setNonFunc(proposal.getNonFunc() != null ? new ArrayList<>(proposal.getFunc()) : null );
//
//        historyRepository.save(history);
//        log.info("Proposal history created for rejected proposal {}", proposal.getId());
//    }
//}
