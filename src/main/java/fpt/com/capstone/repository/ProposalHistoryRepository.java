package fpt.com.capstone.repository;

import fpt.com.capstone.model.CapstoneProposalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalHistoryRepository extends JpaRepository<CapstoneProposalHistory,Integer> {
}
