package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LecturerCouncil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name  = "lecturer_id")
    @ManyToOne
    private Lecturer lecturer;

    @JoinColumn(name = "council_id")
    @ManyToOne
    private Council council;
}
