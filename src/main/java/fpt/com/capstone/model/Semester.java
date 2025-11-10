package fpt.com.capstone.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private boolean isCurrent;
    private String semesterCode;
    private int academic_year;
    private Date StartDate;
    private Date EndDate;
    String reviewerCode1;
    String reviewerCode2;
    String reviewerCode3;
    String reviewerCode4;
//    private boolean isClosed;
}
