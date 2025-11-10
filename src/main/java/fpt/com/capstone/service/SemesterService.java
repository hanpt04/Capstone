package fpt.com.capstone.service;

import fpt.com.capstone.model.Semester;
import fpt.com.capstone.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;

    public Semester save (@RequestBody Semester semester) {
        return semesterRepository.save(semester);
    }
    public List<Semester> getAll() {
        return semesterRepository.findAll();
    }
    public Semester findById(int id) {
        return semesterRepository.findById(id).orElse(null);    }


}
