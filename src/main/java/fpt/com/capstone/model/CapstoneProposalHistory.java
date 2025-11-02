package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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
    private String attachmentUrl;

    @ElementCollection
    private List<String> nonFunc;
    @ElementCollection
    private List<String> func;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

