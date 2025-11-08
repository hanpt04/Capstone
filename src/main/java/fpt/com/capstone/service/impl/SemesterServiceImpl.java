package fpt.com.capstone.service.impl;

import fpt.com.capstone.model.Semester;
import fpt.com.capstone.repository.SemesterRepository;
import fpt.com.capstone.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public Semester save (Semester semester) {
        return semesterRepository.save(semester);
    }

    @Override
    public List<Semester> getAll() {
        return semesterRepository.findAll();
    }

    @Override
    public Semester findById(int id) {
        return semesterRepository.findById(id).orElse(null);    }
}
