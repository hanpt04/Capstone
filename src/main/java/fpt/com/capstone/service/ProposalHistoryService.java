package fpt.com.capstone.service;

import fpt.com.capstone.model.CapstoneProposalHistory;

import java.util.List;

public interface ProposalHistoryService {
    CapstoneProposalHistory getProposalHistory(int proposalId);
    List<CapstoneProposalHistory> getAllProposalHistory();
    CapstoneProposalHistory save(CapstoneProposalHistory proposalHistory);
    CapstoneProposalHistory update(CapstoneProposalHistory proposalHistory);
    List< CapstoneProposalHistory> getHistoriesByProposalId(int proposalId);
}
