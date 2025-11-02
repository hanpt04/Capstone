package fpt.com.capstone.service;

import fpt.com.capstone.model.CapstoneProposalHistory;
import fpt.com.capstone.repository.CapstoneProposalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalHistoryService {
    @Autowired
    private CapstoneProposalHistoryRepository capstoneProposalHistoryRepository;

    public CapstoneProposalHistory getProposalHistory(int proposalId) {
        return capstoneProposalHistoryRepository.findById(proposalId).orElseThrow(() ->
            new RuntimeException("Proposal history not found for id: " + proposalId)
        );
    }

    public List<CapstoneProposalHistory> getAllProposalHistory() {
        return capstoneProposalHistoryRepository.findAll();
    }

    public CapstoneProposalHistory save(CapstoneProposalHistory proposalHistory) {
        return capstoneProposalHistoryRepository.save(proposalHistory);
    }

    public CapstoneProposalHistory update(CapstoneProposalHistory proposalHistory) {
        return capstoneProposalHistoryRepository.save(proposalHistory);
    }


    public List< CapstoneProposalHistory> getHistoriesByProposalId(int proposalId) {
        return capstoneProposalHistoryRepository.findByCapstoneProposalId(proposalId);
    }

}
