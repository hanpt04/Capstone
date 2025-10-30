package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "capstone_proposals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapstoneProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Nationalized
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
    private String attachmentUrl;

    @Column(columnDefinition = "TEXT")
    private String nonFunc;
    @Column(columnDefinition = "TEXT")
    private String Func;

    private boolean isAdmin1 =false;
    private boolean isAdmin2 =false;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum ProposalStatus {
        DUPLICATE_REJECTED //bị trùng
        , DUPLICATE_ACCEPTED //đc qua vòng check trùng
        ,REVIEW_1 //review lần 1
        ,REVIEW_2 //review lần 2
        ,REVIEW_3 //review lần 3
        ,DEFENSE // ra hội đồng lần 1
        ,FINAL_DEFENSE //ra hội đồng lần 2
        ,FAILED //bị đánh rớt
        ,COMPLETED //hoàn thành
    }


}
