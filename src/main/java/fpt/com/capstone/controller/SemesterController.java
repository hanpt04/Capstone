package fpt.com.capstone.controller;

import fpt.com.capstone.model.Semester;
import fpt.com.capstone.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semester")
public class SemesterController {
    @Autowired
    private SemesterService semesterService;

    @GetMapping
    public ResponseEntity<List<Semester>> getAll () {
        List<Semester> semesters = semesterService.getAll();
        return ResponseEntity.ok(semesters);
    }

    @PostMapping
    public ResponseEntity<Semester> createSemester(Semester semester) {
        Semester createdSemester = semesterService.save(semester);
        return ResponseEntity.ok(createdSemester);
    }
    @GetMapping("{id}")
    public ResponseEntity<Semester> findById(@PathVariable int id) {
        return ResponseEntity.ok(semesterService.findById(id));}

}
