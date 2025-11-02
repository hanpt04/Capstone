package fpt.com.capstone.repository;

import fpt.com.capstone.model.CapstoneProposalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CapstoneProposalHistoryRepository extends JpaRepository<CapstoneProposalHistory,Integer> {
    List<CapstoneProposalHistory> findByCapstoneProposalId(int proposalId);
}
