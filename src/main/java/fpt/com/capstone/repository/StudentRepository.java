package fpt.com.capstone.repository;

import fpt.com.capstone.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
