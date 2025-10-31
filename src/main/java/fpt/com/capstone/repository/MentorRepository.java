package fpt.com.capstone.repository;

import fpt.com.capstone.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Integer> {
    Optional<Mentor> findByMentorCode(String mentorCode);
    Boolean existsByMentorCode(String mentorCode);
}
