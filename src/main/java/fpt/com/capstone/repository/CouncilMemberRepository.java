package fpt.com.capstone.repository;

import fpt.com.capstone.model.CouncilMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouncilMemberRepository extends JpaRepository<CouncilMember, Integer> {
}
