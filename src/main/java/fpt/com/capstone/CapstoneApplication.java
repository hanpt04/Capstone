package fpt.com.capstone;

import fpt.com.capstone.model.Account;
import fpt.com.capstone.model.Student;
import fpt.com.capstone.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CapstoneApplication implements CommandLineRunner {

    private final StudentRepository studentRepository;

    public CapstoneApplication(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CapstoneApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
