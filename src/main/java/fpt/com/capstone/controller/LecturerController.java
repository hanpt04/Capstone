package fpt.com.capstone.controller;


import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.repository.LecturerRepository;
import fpt.com.capstone.service.LectuterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lecturers")
@RequiredArgsConstructor
public class LecturerController {

    private final LectuterService lectuterService;

    @GetMapping
    public List<Lecturer> getAllLecturers() {
        return lectuterService.getAllLecturers();
    }

}
