//package fpt.com.capstone.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//
//import java.time.LocalDateTime;
//
//@Data
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//public class CouncilReview {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @ManyToOne
//    @JoinColumn(name = "semester_id")
//    Semester semester;
//    int capstoneProposalId;
//    LocalDateTime reviewAt;
//    String reviewerCode1;
//    String reviewerCode2;
//    String reviewerCode3;
//    String reviewerCode4;
//}
