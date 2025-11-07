package fpt.com.capstone;

import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.model.Semester;
import fpt.com.capstone.repository.LecturerRepository;
import fpt.com.capstone.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CapstoneApplication implements CommandLineRunner {


    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    LecturerRepository lecturerRepository;


    public static void main(String[] args) {
        SpringApplication.run(CapstoneApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println( "Application started successfully!" );
//        semesterRepository.save( new Semester(0, "Spring 2024", true, "SP24", 2024, java.sql.Date.valueOf("2024-01-01"), java.sql.Date.valueOf("2024-05-31")) );
//        lecturerRepository.save( new Lecturer( 0, "1234","12345678@gmail.com","admin","0987678987", Lecturer.AccountRole.ADMIN,true,null,null,"ADMIN002",null )) ;
////        lecturerRepository.save( new Lecturer( 0, "5678","5678@gmail.com","mentor","0987654321", Lecturer.AccountRole.MENTOR,true,null,null,"MENTOR001",null )) ;
//        System.out.println(lecturerRepository.findAll());
    }
}
