package fpt.com.capstone;

import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.model.Semester;
import fpt.com.capstone.repository.LecturerRepository;
import fpt.com.capstone.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // <-- THÊM DÒNG NÀY
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
//        System.out.println( "Application started successfully!" );
//        semesterRepository.save( new Semester(0, "Spring 2024", true, "SP24", 2024, java.sql.Date.valueOf("2024-01-01"), java.sql.Date.valueOf("2024-05-31")) );
//// Giảng viên hướng dẫn (MENTOR)
//        lecturerRepository.save( new Lecturer( 0, "pass123","mentor.a@gmail.com","Trần Văn An","0912345678", Lecturer.AccountRole.ADMIN,true,null,null,"admin1" )) ;
//        lecturerRepository.save( new Lecturer( 0, "pass456","mentor.b@gmail.com","Nguyễn Thị Bình","0912345679", Lecturer.AccountRole.MENTOR,true,null,null,"MENTOR002" )) ;
//        lecturerRepository.save( new Lecturer( 0, "pass222","mento222r.b@gmail.com","Nguyễn Thị A","0912345679", Lecturer.AccountRole.MENTOR,true,null,null,"MENTOR003" )) ;
//
//// Giảng viên (LECTURER)
//        lecturerRepository.save( new Lecturer( 0, "pass789","lecturer.c@gmail.com","Lê Minh Cường","0905111222", Lecturer.AccountRole.MENTOR,true,null,null,"MENTOR004" )) ;
//        lecturerRepository.save( new Lecturer( 0, "pass101","lecturer.d@gmail.com","Phạm Thu Duyên","0905333444", Lecturer.AccountRole.MENTOR,true,null,null,"MENTOR005" )) ;
//        lecturerRepository.save( new Lecturer( 0, "pass112","lecturer.e@gmail.com","Đỗ Văn Em","0905555666", Lecturer.AccountRole.MENTOR,true,null,null,"MENTOR006" )) ;//        lecturerRepository.save( new Lecturer( 0, "5678","5678@gmail.com","mentor","0987654321", Lecturer.AccountRole.MENTOR,true,null,null,"MENTOR001",null )) ;
//        System.out.println(lecturerRepository.findAll());
    }
}
