package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CapstoneProposalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "capstone_proposal_id")
    @ManyToOne
    private CapstoneProposal capstoneProposal;
    private String reason; // Reason for the change
    @Nationalized
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
    private String attachmentUrl;

    @Column(columnDefinition = "TEXT")
    private String nonFunc;
    @Column(columnDefinition = "TEXT")
    private String Func;

    private Date rejectDate;
    private boolean isAdmin1;
    private boolean isAdmin2 ;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

