package fpt.com.capstone.service;

import fpt.com.capstone.model.CapstoneProposalHistory;
import fpt.com.capstone.repository.ProposalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalHistoryService {
    @Autowired
    private ProposalHistoryRepository proposalHistoryRepository;

    public CapstoneProposalHistory getProposalHistory(int proposalId) {
        return proposalHistoryRepository.findById(proposalId).orElseThrow(() ->
            new RuntimeException("Proposal history not found for id: " + proposalId)
        );
    }

    public List<CapstoneProposalHistory> getAllProposalHistory() {
        return proposalHistoryRepository.findAll();
    }

    public CapstoneProposalHistory save(CapstoneProposalHistory proposalHistory) {
        return proposalHistoryRepository.save(proposalHistory);
    }

    public CapstoneProposalHistory update(CapstoneProposalHistory proposalHistory) {
        return proposalHistoryRepository.save(proposalHistory);
    }

}
