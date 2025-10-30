package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(name = "students")
@NoArgsConstructor
public class Student extends Account {

    @Column(nullable = false, unique = true)
    private String studentCode;

    private Double gpa;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;


    public Student(String password, String email, String fullName, String phoneNumber, String studentCode, Double gpa, Mentor mentor) {
        super(password, email, fullName, phoneNumber, AccountRole.STUDENT);
        this.gpa=gpa;
        this.studentCode = studentCode;
        this.mentor = mentor;
    }
}
