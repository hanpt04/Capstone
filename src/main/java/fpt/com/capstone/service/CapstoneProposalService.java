package fpt.com.capstone.service;

import fpt.com.capstone.exception.CustomException;
import fpt.com.capstone.exception.DuplicateProposalException;
import fpt.com.capstone.mapper.CapstoneProposalHistoryMapper;
import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.CapstoneProposalHistory;
import fpt.com.capstone.model.DTO.DuplicateCheckResult;
import fpt.com.capstone.model.Semester;
import fpt.com.capstone.repository.CapstoneProposalHistoryRepository;
import fpt.com.capstone.repository.CapstoneProposalRepository;
import fpt.com.capstone.util.ProposalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CapstoneProposalService {

    private final CapstoneProposalRepository capstoneProposalRepository;
    private final ChromaDBService chromaDBService;
    private final CapstoneProposalHistoryMapper historyMapper;
    private final CapstoneProposalHistoryRepository historyRepository;

    public List<CapstoneProposal> getAll() {
        return capstoneProposalRepository.findAll();
    }

    public List<CapstoneProposal> getProposalsByReviewer(String lecturerCode) {
        return capstoneProposalRepository.findByAnyReviewerCode(lecturerCode);
    }




    public CapstoneProposal updateReview (int id, LocalDateTime date, int reviewTime, String  mentorCode1, String mentorCode2, String mentorName1, String mentorName2) {
        CapstoneProposal proposal = capstoneProposalRepository.findById(id).orElseThrow(() -> new CustomException("Proposal not found", HttpStatus.NOT_FOUND));

        CapstoneProposal.Reviewer existedReviewer;
        if ( proposal.getReviewer() == null) {
            existedReviewer = new CapstoneProposal.Reviewer();
        } else
            existedReviewer = proposal.getReviewer();

        switch ( reviewTime)

        {
            case 1:
                proposal.setReview1At( date);
                existedReviewer.setReviewer1Code( mentorCode1);
                existedReviewer.setReviewer1Name( mentorName1);
                existedReviewer.setReviewer2Code( mentorCode2);
                existedReviewer.setReviewer2Name( mentorName2);
                proposal.setReviewer( existedReviewer);
                break;
            case 2:
                proposal.setReview2At( date);
                existedReviewer.setReviewer3Code( mentorCode1);
                existedReviewer.setReviewer3Name( mentorName1);
                existedReviewer.setReviewer4Code( mentorCode2);
                existedReviewer.setReviewer4Name( mentorName2);
                proposal.setReviewer( existedReviewer);
                break;
            case 3:
                proposal.setReview3At( date);
                existedReviewer.setReviewer5Code( mentorCode1);
                existedReviewer.setReviewer5Name( mentorName1);
                existedReviewer.setReviewer6Code( mentorCode2);
                existedReviewer.setReviewer6Name( mentorName2);
                proposal.setReviewer( existedReviewer);
                break;
            default:
                throw new CustomException("Invalid review time", HttpStatus.BAD_REQUEST);
        }
        return capstoneProposalRepository.save(proposal);
    }

    public CapstoneProposal save(CapstoneProposal proposal) {

        // save

        CapstoneProposal saved;

        if (proposal.getId() != null ) {
            // UPDATE: Load từ DB
            CapstoneProposal existing = capstoneProposalRepository.findById(proposal.getId()).orElseThrow(() -> new CustomException("Proposal not found", HttpStatus.NOT_FOUND));
            ProposalUtils.copyUpdatableFields(existing, proposal);
            saved = capstoneProposalRepository.save(existing);

        } else {
            // CREATE: Tạo mới
            saved = capstoneProposalRepository.save(proposal);
        }

        // Check duplicate
        DuplicateCheckResult checkResult = chromaDBService.checkDuplicate(proposal);
        if (checkResult.isDuplicate()) {

            //Set Id proposal sau khi luu de tra ve FE
            checkResult.setCurrtentId( saved.getId());

            CapstoneProposalHistory history = historyMapper.toHistory(saved, "Proposal bị trùng lặp với proposal ID: " + checkResult.getClosestId());
            historyRepository.save( history);
            CapstoneProposal existing = capstoneProposalRepository.findById(proposal.getId()).orElseThrow(() -> new CustomException("Proposal not found", HttpStatus.NOT_FOUND));
            existing.setStatus( CapstoneProposal.ProposalStatus.DUPLICATE_REJECTED);
            capstoneProposalRepository.save(existing);
            throw new DuplicateProposalException("Proposal bị trùng lặp!", checkResult);
        }
        else
        {
            CapstoneProposal existing = capstoneProposalRepository.findById(proposal.getId()).orElseThrow(() -> new CustomException("Proposal not found", HttpStatus.NOT_FOUND));
           existing.setStatus( CapstoneProposal.ProposalStatus.DUPLICATE_ACCEPTED );
            capstoneProposalRepository.save(existing);
        }



        // Upload ChromaDB
        boolean uploaded = chromaDBService.uploadProposal(saved);

        if (uploaded) {
            return saved;
        }
        throw new CustomException("Failed to upload proposal to ChromaDB", HttpStatus.BAD_REQUEST);
    }

//    @Transactional
//    public CapstoneProposal reviewProposal(int proposalId, Boolean isApproved, String reason, int adminId) {
//
//        if (!isApproved && (reason == null || reason.trim().isEmpty())) {
//            throw new CustomException("Reason is required when rejecting", HttpStatus.BAD_REQUEST);
//        }
//
//        CapstoneProposal proposal = capstoneProposalRepository.findById(proposalId).orElseThrow(() -> new CustomException("Proposal not found", HttpStatus.NOT_FOUND));
//
//        if ( !isApproved)
//        {
//            proposal.setStatus(CapstoneProposal.ProposalStatus.REJECT_BY_ADMIN);
//             CapstoneProposalHistory capstoneProposalHistory =   historyMapper.toHistory(proposal, " Reject By Admin: " + adminId + ", Reason: " + reason);
//             capstoneProposalHistory.setAdminRejectId( adminId);
//             historyRepository.save( capstoneProposalHistory);
//            return capstoneProposalRepository.save(proposal);
//        }
//        else
//        {
//            if (proposal.getIsAdmin1()!=null) {
//                // Lần approve thứ 2
//                proposal.setIsAdmin2(true);
//                proposal.setStatus(CapstoneProposal.ProposalStatus.REVIEW_1);
//                proposal.setAdmin2Id(adminId);
//            }
//            else
//            {
//                // Lần approve đầu tiên
//                proposal.setIsAdmin1(true);
//                proposal.setAdmin1Id( adminId);
//            }
//        }
//
//        return capstoneProposalRepository.save(proposal);
//    }

    public CapstoneProposal findById(int id) {
        return capstoneProposalRepository.findById(id).orElse(null);
    }


//    public List<CapstoneProposal> getForAdminAprove(Integer adminId) {
//       List<CapstoneProposal> list = capstoneProposalRepository.findAllByAdmin1IdIsNullOrAdmin2IdIsNullAndAdmin1IdNotOrAdmin2IdNot(adminId, adminId);
//       List<CapstoneProposal> returnList = new ArrayList<>();
//       for( CapstoneProposal p : list)
//       {
//           if(p.getStatus() == CapstoneProposal.ProposalStatus.SUBMITTED || p.getStatus() == CapstoneProposal.ProposalStatus.DUPLICATE_ACCEPTED ){
//                returnList.add( p );
//           }
//       }
//         return returnList;
//    }


    public CapstoneProposal reviewProposal(int proposalId, Boolean isApproved, String reason, String reviewerCode) {

        // Validate rejection reason
        if (!isApproved && (reason == null || reason.trim().isEmpty())) {
            throw new CustomException("Reason is required when rejecting", HttpStatus.BAD_REQUEST);
        }

        // Get proposal
        CapstoneProposal proposal = capstoneProposalRepository.findById(proposalId)
                .orElseThrow(() -> new CustomException("Proposal not found", HttpStatus.NOT_FOUND));

        // Get semester to validate reviewer
        Semester semester = proposal.getSemester();
        if (semester == null) {
            throw new CustomException("Proposal has no semester assigned", HttpStatus.BAD_REQUEST);
        }

        // Validate reviewer belongs to semester
        if (!isReviewerValid(reviewerCode, semester)) {
            throw new CustomException("Reviewer is not assigned to this semester", HttpStatus.FORBIDDEN);
        }

        // Check if reviewer already reviewed this proposal
        if (hasReviewerAlreadyReviewed(proposal, reviewerCode)) {
            throw new CustomException("Reviewer has already reviewed this proposal", HttpStatus.BAD_REQUEST);
        }

        // Handle rejection case - pending business logic from QA
        if (!isApproved) {
            // TODO: Implement reject logic when QA provides business requirements
            throw new CustomException("Rejection flow is pending QA clarification", HttpStatus.NOT_IMPLEMENTED);
        }

        // Handle approval
        recordReviewerApproval(proposal, reviewerCode);

        // Check how many approvals we have now
        int approvalCount = countApprovals(proposal);

        // If we have 2 approvals, change status to REVIEW_1
        if (approvalCount == 2) {
            proposal.setStatus(CapstoneProposal.ProposalStatus.REVIEW_1);
            proposal.setReview1At(LocalDateTime.now());

            // Save history
            CapstoneProposalHistory history = historyMapper.toHistory(
                    proposal,
                    "Proposal approved by 2 reviewers. Changed status to REVIEW_1"
            );
            historyRepository.save(history);
        }

        return capstoneProposalRepository.save(proposal);
    }

    /**
     * Check if reviewer code is one of the 4 reviewers in the semester
     */
    private boolean isReviewerValid(String reviewerCode, Semester semester) {
        return reviewerCode.equals(semester.getReviewerCode1()) ||
                reviewerCode.equals(semester.getReviewerCode2()) ||
                reviewerCode.equals(semester.getReviewerCode3()) ||
                reviewerCode.equals(semester.getReviewerCode4());
    }

    /**
     * Check if this reviewer has already reviewed this proposal
     */
    private boolean hasReviewerAlreadyReviewed(CapstoneProposal proposal, String reviewerCode) {
        if (proposal.getReviewer() == null) {
            return false;
        }

        CapstoneProposal.Reviewer reviewer = proposal.getReviewer();

        // Check if reviewer code exists in any of the reviewer slots
        if (reviewerCode.equals(reviewer.getReviewer1Code()) && proposal.getIsReviewerApprove1() != null) {
            return true;
        }
        if (reviewerCode.equals(reviewer.getReviewer2Code()) && proposal.getIsReviewerApprove2() != null) {
            return true;
        }
        if (reviewerCode.equals(reviewer.getReviewer3Code()) && proposal.getIsReviewerApprove3() != null) {
            return true;
        }
        if (reviewerCode.equals(reviewer.getReviewer4Code()) && proposal.getIsReviewerApprove4() != null) {
            return true;
        }

        return false;
    }

    /**
     * Record reviewer approval in the proposal
     */
    private void recordReviewerApproval(CapstoneProposal proposal, String reviewerCode) {
        // Initialize reviewer if not exists
        if (proposal.getReviewer() == null) {
            proposal.setReviewer(new CapstoneProposal.Reviewer());
        }

        CapstoneProposal.Reviewer reviewer = proposal.getReviewer();

        // Find first available slot and record approval
        if (reviewer.getReviewer1Code() == null || reviewer.getReviewer1Code().equals(reviewerCode)) {
            reviewer.setReviewer1Code(reviewerCode);
            proposal.setIsReviewerApprove1(true);
        } else if (reviewer.getReviewer2Code() == null || reviewer.getReviewer2Code().equals(reviewerCode)) {
            reviewer.setReviewer2Code(reviewerCode);
            proposal.setIsReviewerApprove2(true);
        } else if (reviewer.getReviewer3Code() == null || reviewer.getReviewer3Code().equals(reviewerCode)) {
            reviewer.setReviewer3Code(reviewerCode);
            proposal.setIsReviewerApprove3(true);
        } else if (reviewer.getReviewer4Code() == null || reviewer.getReviewer4Code().equals(reviewerCode)) {
            reviewer.setReviewer4Code(reviewerCode);
            proposal.setIsReviewerApprove4(true);
        }
    }

    /**
     * Count how many reviewers have approved
     */
    private int countApprovals(CapstoneProposal proposal) {
        int count = 0;

        if (Boolean.TRUE.equals(proposal.getIsReviewerApprove1())) {
            count++;
        }
        if (Boolean.TRUE.equals(proposal.getIsReviewerApprove2())) {
            count++;
        }
        if (Boolean.TRUE.equals(proposal.getIsReviewerApprove3())) {
            count++;
        }
        if (Boolean.TRUE.equals(proposal.getIsReviewerApprove4())) {
            count++;
        }

        return count;
    }

}
