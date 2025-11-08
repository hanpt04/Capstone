package fpt.com.capstone.service;

import fpt.com.capstone.model.Semester;

import java.util.List;

public interface SemesterService {
    Semester save (Semester semester);
    List<Semester> getAll();
    Semester findById(int id);
}
