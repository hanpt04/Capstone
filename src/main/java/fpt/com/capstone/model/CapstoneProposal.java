package fpt.com.capstone.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "capstone_proposals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapstoneProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    @Nationalized
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String lecturerCode1 = "LAMNN23";
    private String lecturerCode2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status = ProposalStatus.SUBMITTED;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
    private String attachmentUrl;

    @ElementCollection
    private List<String> nonFunc;
    @ElementCollection
    private List<String> func;

    @Embedded
    private ProposalStudents students;

    Boolean isReviewerApprove1;
    Boolean isReviewerApprove2;
    Boolean isReviewerApprove3;
    Boolean isReviewerApprove4;


    @Embedded
    Reviewer reviewer;

    @Nullable
    private LocalDateTime review1At;

    @Nullable
    private LocalDateTime review2At;
    @Nullable
    private LocalDateTime review3At;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum ProposalStatus {
        SUBMITTED,// Moi nop
        DUPLICATE_REJECTED //bị trùng
        ,REJECT_BY_ADMIN //bị admin từ chối
        ,DUPLICATE_ACCEPTED //đc qua vòng check trùng
        ,REVIEW_1 //review lần 1
        ,REVIEW_2 //review lần 2
        ,REVIEW_3 //review lần 3
        ,DEFENSE // ra hội đồng lần 1
        ,SECOND_DEFENSE //ra hội đồng lần 2
        ,FAILED //bị đánh rớt
        ,COMPLETED //hoàn thành
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProposalStudents {

        private String student1Id;
        private String student1Name;
        private String student1Phone;

        private String student2Id;
        private String student2Name;
        private String student2Phone;

        private String student3Id;
        private String student3Name;
        private String student3Phone;

        private String student4Id;
        private String student4Name;
        private String student4Phone;

        private String student5Id;
        private String student5Name;
        private String student5Phone;

        private String student6Id;
        private String student6Name;
        private String student6Phone;
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reviewer {

        private String reviewer1Code;
        private String reviewer1Name;

        private String reviewer2Code;
        private String reviewer2Name;

        private String reviewer3Code;
        private String reviewer3Name;

        private String reviewer4Code;
        private String reviewer4Name;

        private String reviewer5Code;
        private String reviewer5Name;

        private String reviewer6Code;
        private String reviewer6Name;


    }
}



