package fpt.com.capstone.repository;

import fpt.com.capstone.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByStudentCode(String studentCode);
    Boolean existsByStudentCode(String studentCode);
}
