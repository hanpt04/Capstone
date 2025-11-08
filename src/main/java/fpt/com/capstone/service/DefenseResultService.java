package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.CreateDefenseResultRequest;
import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.DefenseResult;
import fpt.com.capstone.model.Schedule;
import fpt.com.capstone.repository.CapstoneProposalRepository;
import fpt.com.capstone.repository.DefenseResultRepository;
import fpt.com.capstone.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefenseResultService {

    private final DefenseResultRepository defenseResultRepository;
    private final ScheduleRepository scheduleRepository;
    private final CapstoneProposalRepository capstoneProposalRepository;


    @Transactional
    public DefenseResult createDefenseResult(CreateDefenseResultRequest request) {

        Schedule schedule = scheduleRepository.findById(request.getScheduleId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch (schedule) với ID: " + request.getScheduleId()));

        if (defenseResultRepository.existsByScheduleId(request.getScheduleId())) {
            throw new IllegalStateException("Lịch này đã có kết quả. Không thể tạo trùng.");
        }

        DefenseResult result = new DefenseResult();
        result.setSchedule(schedule);
        result.setStatus(request.getStatus());
        result.setScore(request.getScore());
        result.setComments(request.getComments());

        CapstoneProposal proposal = schedule.getCapstoneProposal();

        if (request.getStatus().equals(DefenseResult.DefenseStatus.PASS)) {
            proposal.setStatus(CapstoneProposal.ProposalStatus.COMPLETED);
        } else {
            if (schedule.getDefenseRound() == 1) {
                // Tạch đợt 1
                proposal.setStatus(CapstoneProposal.ProposalStatus.SECOND_DEFENSE);
            } else {
                // Tạch đợt 2
                proposal.setStatus(CapstoneProposal.ProposalStatus.FAILED);
            }
        }
        capstoneProposalRepository.save(proposal);
        return defenseResultRepository.save(result);
    }

    public List<DefenseResult> getAllDefenseResults() {
        return defenseResultRepository.findAll();
    }

    public List<DefenseResult> getDefenseResultsByProposalId(int proposalId) {
        return defenseResultRepository.findByScheduleCapstoneProposalId(proposalId);
    }
}
