package fpt.com.capstone.repository;


import fpt.com.capstone.model.CapstoneProposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapstoneProposalRepository extends JpaRepository<CapstoneProposal, Integer> {
    Page<CapstoneProposal> findByStatus(CapstoneProposal.ProposalStatus status, Pageable pageable);

    Page<CapstoneProposal> findByStatusIn(List<CapstoneProposal.ProposalStatus> statuses, Pageable pageable);

    List<CapstoneProposal> findAllByAdmin1IdIsNullOrAdmin2IdIsNullAndAdmin1IdNotOrAdmin2IdNot(Integer admin1Id, Integer admin2Id);

}
