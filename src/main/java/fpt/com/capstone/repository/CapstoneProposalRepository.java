package fpt.com.capstone.repository;


import fpt.com.capstone.model.CapstoneProposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapstoneProposalRepository extends JpaRepository<CapstoneProposal, Integer> {
    Page<CapstoneProposal> findByStatus(CapstoneProposal.ProposalStatus status, Pageable pageable);

    Page<CapstoneProposal> findByStatusIn(List<CapstoneProposal.ProposalStatus> statuses, Pageable pageable);

    List<CapstoneProposal> findAllByAdmin1IdIsNullOrAdmin2IdIsNullAndAdmin1IdNotOrAdmin2IdNot(Integer admin1Id, Integer admin2Id);

    @Query("SELECT p FROM CapstoneProposal p WHERE " +
            "p.reviewer.reviewer1Code = :code OR " +
            "p.reviewer.reviewer2Code = :code OR " +
            "p.reviewer.reviewer3Code = :code OR " +
            "p.reviewer.reviewer4Code = :code OR " +
            "p.reviewer.reviewer5Code = :code OR " +
            "p.reviewer.reviewer6Code = :code")
    List<CapstoneProposal> findByAnyReviewerCode(@Param("code") String code);

    List<CapstoneProposal> findByStatusIn(List<CapstoneProposal.ProposalStatus> statuses);
}
