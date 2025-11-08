package fpt.com.capstone.service;

import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.repository.LecturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectuterService {

    private final LecturerRepository lecturerRepository;



    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }

    public Lecturer getLecturerById(int id) {
        return lecturerRepository.findById(id).orElse(null);
    }
}
