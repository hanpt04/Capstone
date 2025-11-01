package fpt.com.capstone.repository;

import fpt.com.capstone.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    Optional<Lecturer> findByEmail(String email);
}
