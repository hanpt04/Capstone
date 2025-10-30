package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapstoneReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "proposal_id")
    @ManyToOne
    private CapstoneProposal proposal;
    private Date reviewDate;
    private String comments;
    @JoinColumn(name = "lecturer_id")
    @ManyToOne
    private Lecturer lecturer;
    private boolean isPass;
    private int reviewRow;

}

