package fpt.com.capstone.service;

import fpt.com.capstone.exception.CustomException;
import fpt.com.capstone.exception.DuplicateProposalException;
import fpt.com.capstone.mapper.CapstoneProposalHistoryMapper;
import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.CapstoneProposalHistory;
import fpt.com.capstone.model.DTO.DuplicateCheckResult;
import fpt.com.capstone.repository.CapstoneProposalHistoryRepository;
import fpt.com.capstone.repository.CapstoneProposalRepository;
import fpt.com.capstone.util.ProposalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CapstoneProposal reviewProposal(int proposalId, Boolean isApproved, String reason, int adminId) {

        if (!isApproved && (reason == null || reason.trim().isEmpty())) {
            throw new CustomException("Reason is required when rejecting", HttpStatus.BAD_REQUEST);
        }

        CapstoneProposal proposal = capstoneProposalRepository.findById(proposalId).orElseThrow(() -> new CustomException("Proposal not found", HttpStatus.NOT_FOUND));

        if ( !isApproved)
        {
            proposal.setStatus(CapstoneProposal.ProposalStatus.REJECT_BY_ADMIN);
             CapstoneProposalHistory capstoneProposalHistory =   historyMapper.toHistory(proposal, " Reject By Admin: " + adminId + ", Reason: " + reason);
             capstoneProposalHistory.setAdminRejectId( adminId);
             historyRepository.save( capstoneProposalHistory);
            return capstoneProposalRepository.save(proposal);
        }
        else
        {
            if (proposal.getIsAdmin1()!=null)
            {
                // Lần approve thứ 2
                proposal.setIsAdmin2(true);
                proposal.setStatus(CapstoneProposal.ProposalStatus.REVIEW_1);
                proposal.setAdmin2Id(adminId);
            }
            else
            {
                // Lần approve đầu tiên
                proposal.setIsAdmin1(true);
                proposal.setAdmin1Id( adminId);
            }
        }

        return capstoneProposalRepository.save(proposal);
    }

    public CapstoneProposal findById(int id) {
        return capstoneProposalRepository.findById(id).orElse(null);
    }

}
