package fpt.com.capstone.service.impl;


import fpt.com.capstone.dto.request.CreateScheduleRequest;
import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.Council;
import fpt.com.capstone.model.Schedule;
import fpt.com.capstone.repository.CapstoneProposalRepository;
import fpt.com.capstone.repository.CouncilRepository;
import fpt.com.capstone.repository.ScheduleRepository;
import fpt.com.capstone.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    CouncilRepository councilRepository;
    @Autowired
    CapstoneProposalRepository capstoneProposalRepository;

    @Override
    @Transactional
    public Schedule createSchedule(CreateScheduleRequest request) {

        if (request.getStartTime().isAfter(request.getEndTime()) || request.getStartTime().equals(request.getEndTime())) {
            throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu.");
        }

        Council council = councilRepository.findById(request.getCouncilId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hội đồng với ID: " + request.getCouncilId()));

        CapstoneProposal project = capstoneProposalRepository.findById(request.getCapstoneProjectId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đồ án với ID: " + request.getCapstoneProjectId()));

        checkCouncilConflict(council.getId(), request.getDefenseDate(), request.getStartTime(), request.getEndTime());

        checkRoomConflict(request.getRoom(), request.getDefenseDate(), request.getStartTime(), request.getEndTime());


        if (scheduleRepository.existsByCapstoneProposalId(project.getId())) {
            throw new IllegalStateException("Đồ án này đã được xếp lịch rồi.");
        }

        Schedule schedule = new Schedule();
        schedule.setCouncil(council);
        schedule.setCapstoneProposal(project);
        schedule.setDefenseDate(request.getDefenseDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setRoom(request.getRoom());
        schedule.setStatus(Schedule.ScheduleStatus.SCHEDULED);

        return  scheduleRepository.save(schedule);
    }

    private void checkCouncilConflict(int councilId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // Tìm tất cả lịch khác của hội đồng này trong cùng ngày
        List<Schedule> existingSchedules = scheduleRepository
                .findByCouncilIdAndDefenseDate(councilId, date);

        for (Schedule existing : existingSchedules) {
            // Check logic overlap: (StartA < EndB) and (StartB < EndA)
            if (startTime.isBefore(existing.getEndTime()) &&
                    existing.getStartTime().isBefore(endTime)) {

                throw new IllegalStateException(
                        "Xung đột lịch: Hội đồng đã bận từ " +
                                existing.getStartTime() + " đến " + existing.getEndTime()
                );
            }
        }
    }

    private void checkRoomConflict(String room, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // Tìm tất cả lịch khác trong cùng phòng, cùng ngày
        List<Schedule> existingSchedules = scheduleRepository
                .findByRoomAndDefenseDate(room, date);

        for (Schedule existing : existingSchedules) {
            // Check logic overlap
            if (startTime.isBefore(existing.getEndTime()) &&
                    existing.getStartTime().isBefore(endTime)) {

                throw new IllegalStateException(
                        "Xung đột lịch: Phòng " + room + " đã được sử dụng từ " +
                                existing.getStartTime() + " đến " + existing.getEndTime()
                );
            }
        }
    }

    @Override
    public List< Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}
