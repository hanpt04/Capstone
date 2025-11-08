package fpt.com.capstone.service.impl;

import fpt.com.capstone.model.CapstoneProposalHistory;
import fpt.com.capstone.repository.CapstoneProposalHistoryRepository;
import fpt.com.capstone.service.ProposalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalHistoryServiceImpl implements ProposalHistoryService {
    @Autowired
    private CapstoneProposalHistoryRepository capstoneProposalHistoryRepository;

    @Override
    public CapstoneProposalHistory getProposalHistory(int proposalId) {
        return capstoneProposalHistoryRepository.findById(proposalId).orElseThrow(() ->
            new RuntimeException("Proposal history not found for id: " + proposalId)
        );
    }

    @Override
    public List<CapstoneProposalHistory> getAllProposalHistory() {
        return capstoneProposalHistoryRepository.findAll();
    }

    @Override
    public CapstoneProposalHistory save(CapstoneProposalHistory proposalHistory) {
        return capstoneProposalHistoryRepository.save(proposalHistory);
    }

    @Override
    public CapstoneProposalHistory update(CapstoneProposalHistory proposalHistory) {
        return capstoneProposalHistoryRepository.save(proposalHistory);
    }

    @Override
    public List< CapstoneProposalHistory> getHistoriesByProposalId(int proposalId) {
        return capstoneProposalHistoryRepository.findByCapstoneProposalId(proposalId);
    }

}
