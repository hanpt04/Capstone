package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProposalLecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "lecturer_id")
    @ManyToOne
    private Lecturer lecturer;

    @JoinColumn(name = "proposal_id")
    @ManyToOne
    private CapstoneProposal proposal;

    private Date assignDate;

}
