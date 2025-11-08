package fpt.com.capstone.repository;

import fpt.com.capstone.model.DefenseResult;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefenseResultRepository  extends JpaRepository<DefenseResult, Integer> {
    boolean existsByScheduleId(@NotNull(message = "ID của Lịch (Schedule) không được để trống") Integer scheduleId);


    List<DefenseResult> findByScheduleCapstoneProposalId(int proposalId);
}
