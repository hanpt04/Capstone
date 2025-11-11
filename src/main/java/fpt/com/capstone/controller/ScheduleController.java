package fpt.com.capstone.controller;


import fpt.com.capstone.dto.request.CreateScheduleRequest;
import fpt.com.capstone.model.Schedule;
import fpt.com.capstone.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public Schedule createSchedule(@RequestBody CreateScheduleRequest request) {
        return  scheduleService.createSchedule(request);
    }

}
