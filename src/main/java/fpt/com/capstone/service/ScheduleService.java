package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.CreateScheduleRequest;
import fpt.com.capstone.model.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleService {
    Schedule createSchedule(CreateScheduleRequest request);
    List< Schedule> getAllSchedules();
}
