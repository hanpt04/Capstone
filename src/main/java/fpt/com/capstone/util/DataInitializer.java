package fpt.com.capstone.util;

import fpt.com.capstone.model.*;
import fpt.com.capstone.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final LecturerRepository lecturerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (lecturerRepository.count() > 0) {
            log.info("Data already initialized. Skipping...");
            return;
        }

        log.info("Initializing test data...");

        // ============= CREATE ADMIN =============
        Lecturer admin = new Lecturer();
        admin.setEmail("admin@fpt.edu.vn");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Admin System");
        admin.setPhoneNumber("0123456789");
        admin.setRole(Lecturer.AccountRole.ADMIN);
        admin.setStatus(true);
        admin.setLecturerCode("ADMIN001");
        lecturerRepository.save(admin);
        log.info("✅ Created Admin: {}", admin.getEmail());

        // ============= CREATE MENTOR =============
        Lecturer mentor = new Lecturer();
        mentor.setEmail("mentor@fpt.edu.vn");
        mentor.setPassword(passwordEncoder.encode("mentor123"));
        mentor.setFullName("Nguyễn Văn Mentor");
        mentor.setPhoneNumber("0987654321");
        mentor.setRole(Lecturer.AccountRole.MENTOR);
        mentor.setStatus(true);
        mentor.setLecturerCode("MENTOR001");
        lecturerRepository.save(mentor);
        log.info("✅ Created Mentor: {}", mentor.getEmail());

        // ============= CREATE LECTURER =============
        Lecturer lecturer = new Lecturer();
        lecturer.setEmail("lecturer@fpt.edu.vn");
        lecturer.setPassword(passwordEncoder.encode("lecturer123"));
        lecturer.setFullName("Trần Thị Lecturer");
        lecturer.setPhoneNumber("0912345678");
        lecturer.setRole(Lecturer.AccountRole.LECTURER);
        lecturer.setStatus(true);
        lecturer.setLecturerCode("LEC001");
        lecturerRepository.save(lecturer);
        log.info("✅ Created Lecturer: {}", lecturer.getEmail());

        log.info("=".repeat(70));
        log.info("🎉 Test accounts created successfully:");
        log.info("📧 Admin    - Email: admin@fpt.edu.vn      | Password: admin123");
        log.info("📧 Mentor   - Email: mentor@fpt.edu.vn     | Password: mentor123");
        log.info("📧 Lecturer - Email: lecturer@fpt.edu.vn   | Password: lecturer123");
        log.info("=".repeat(70));
        log.info("📌 All passwords are hashed with BCrypt");
        log.info("=".repeat(70));
    }
}
