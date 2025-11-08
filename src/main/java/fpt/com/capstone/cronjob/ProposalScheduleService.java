package fpt.com.capstone.cronjob;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.repository.CapstoneProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Dùng để log (nên có)
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProposalScheduleService {

    private final CapstoneProposalRepository capstoneProposalRepository;

    @Scheduled(cron = "@daily")
    @Transactional
    public void autoUpdateProposalStatus() {


        LocalDateTime now = LocalDateTime.now();

        List<CapstoneProposal.ProposalStatus> statusesToScan = List.of(
                CapstoneProposal.ProposalStatus.REVIEW_1,
                CapstoneProposal.ProposalStatus.REVIEW_2,
                CapstoneProposal.ProposalStatus.REVIEW_3
        );

        List<CapstoneProposal> proposalsToScan = capstoneProposalRepository.findByStatusIn(statusesToScan);
        List<CapstoneProposal> updatedProposals = new ArrayList<>();

        for (CapstoneProposal proposal : proposalsToScan) {
            CapstoneProposal.ProposalStatus currentStatus = proposal.getStatus();


            if (currentStatus == CapstoneProposal.ProposalStatus.REVIEW_1 && proposal.getReview1At() != null && now.isAfter(proposal.getReview1At()))
            {
                proposal.setStatus(CapstoneProposal.ProposalStatus.REVIEW_2);
                updatedProposals.add(proposal);
            }

            else if (currentStatus == CapstoneProposal.ProposalStatus.REVIEW_2 && proposal.getReview2At() != null && now.isAfter(proposal.getReview2At()))
            {
                proposal.setStatus(CapstoneProposal.ProposalStatus.REVIEW_3);
                updatedProposals.add(proposal);
            }
            else if (currentStatus == CapstoneProposal.ProposalStatus.REVIEW_3 && proposal.getReview3At() != null && now.isAfter(proposal.getReview3At()))
            {
                proposal.setStatus(CapstoneProposal.ProposalStatus.DEFENSE);
                updatedProposals.add(proposal);
            }
        }

        if (!updatedProposals.isEmpty()) {
            capstoneProposalRepository.saveAll(updatedProposals);
        }
    }
}