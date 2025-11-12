package fpt.com.capstone.service;

import fpt.com.capstone.model.Lecturer;

import java.util.List;

public interface LectuterService {
    List<Lecturer> getAllLecturers();
    Lecturer getLecturerById(int id);

    List<Lecturer> saveAll(List<Lecturer> lecturers);

}
