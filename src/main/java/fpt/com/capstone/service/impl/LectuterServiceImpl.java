package fpt.com.capstone.service.impl;

import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.repository.LecturerRepository;
import fpt.com.capstone.service.LectuterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectuterServiceImpl implements LectuterService {

    private final LecturerRepository lecturerRepository;

    @Override
    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }

    @Override
    public Lecturer getLecturerById(int id) {
        return lecturerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Lecturer> saveAll(List<Lecturer> lecturers) {
        return lecturerRepository.saveAll(lecturers);
    }
}
