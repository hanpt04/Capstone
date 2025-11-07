package fpt.com.capstone.repository;

import fpt.com.capstone.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
    boolean existsByCapstoneProposalId(Integer id);

    List<Schedule> findByCouncilIdAndDefenseDate(int councilId, LocalDate date);

    List<Schedule> findByRoomAndDefenseDate(String room, LocalDate date);
}
