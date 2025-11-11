package fpt.com.capstone.controller;


import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.service.LectuterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecturers")
@RequiredArgsConstructor
public class LecturerController {

    private final LectuterService lectuterService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public List<Lecturer> getAllLecturers() {
        return lectuterService.getAllLecturers();
    }

    @GetMapping ("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public Lecturer getLecturerById(@PathVariable int id) {
        return lectuterService.getLecturerById(id);
    }

}
