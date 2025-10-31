package fpt.com.capstone.util;

import fpt.com.capstone.model.*;
import fpt.com.capstone.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final MentorRepository mentorRepository;
    private final SemesterRepository semesterRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (accountRepository.count() > 0) {
            log.info("Data already initialized. Skipping...");
            return;
        }

        log.info("Initializing test data...");

        // Create Mentor
        Mentor mentor = new Mentor(
                passwordEncoder.encode("mentor123"),
                "mentor@fpt.edu.vn",
                "Nguyễn Văn Mentor",
                "0987654321",
                "MENTOR001"
        );
        mentorRepository.save(mentor);
        log.info("Created Mentor: {}", mentor.getEmail());

        // Create Lecturer
        Lecturer lecturer = new Lecturer();
        lecturer.setEmail("lecturer@fpt.edu.vn");
        lecturer.setPassword(passwordEncoder.encode("lecturer123"));
        lecturer.setFullName("Trần Thị Lecturer");
        lecturer.setPhoneNumber("0912345678");
        lecturer.setRole(Account.AccountRole.LECTURER);
        lecturer.setStatus(true);
        lecturer.setLecturerCode("LEC001");
        lecturerRepository.save(lecturer);
        log.info("Created Lecturer: {}", lecturer.getEmail());

        // Create Student
        Student student = new Student(
                passwordEncoder.encode("student123"),
                "student@fpt.edu.vn",
                "Lê Văn Student",
                "0901234567",
                "SE160001",
                3.5,
                mentor
        );
        studentRepository.save(student);
        log.info("Created Student: {}", student.getEmail());

        // Create Current Semester
        Semester semester = new Semester();
        semester.setName("Spring 2025");
        semester.setSemesterCode("SP25");
        semester.setYear(2025);
        semester.setCurrent(true);
        semester.setStartDate(java.sql.Date.valueOf("2025-01-15"));
        semester.setEndDate(java.sql.Date.valueOf("2025-05-15"));
        semesterRepository.save(semester);
        log.info("Created Semester: {}", semester.getName());

        log.info("=".repeat(50));
        log.info("Test accounts created:");
        log.info("Mentor   - Email: mentor@fpt.edu.vn, Password: mentor123");
        log.info("Lecturer - Email: lecturer@fpt.edu.vn, Password: lecturer123");
        log.info("Student  - Email: student@fpt.edu.vn, Password: student123");
        log.info("=".repeat(50));
    }
}
