package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "mentors")
public class Mentor extends Account {

    @Column(nullable = false, unique = true)
    private String mentorCode;


    public Mentor(String password, String email, String fullName, String phoneNumber, String code) {
        super(password, email, fullName, phoneNumber, AccountRole.MENTOR);
        this.mentorCode = code;
    }


}
