package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "lecturers")

public class Lecturer extends Account {

    @Column(nullable = false, unique = true)
    private String lecturerCode;

    // Constructors
    public Lecturer() {
        super();
        setRole(AccountRole.LECTURER);
    }

}
