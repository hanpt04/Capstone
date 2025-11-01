package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MentorProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Lecturer mentor;

    @ManyToOne
    @JoinColumn(name = "proposal_id")
    private CapstoneProposal proposal;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
