package fpt.com.capstone.repository;

import fpt.com.capstone.model.CouncilMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CouncilMemberRepository extends JpaRepository<CouncilMember, Integer> {

    Set<CouncilMember> findByCouncilId(int councilId);
}
