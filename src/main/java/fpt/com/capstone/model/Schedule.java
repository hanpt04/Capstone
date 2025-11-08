package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Schedule   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "capstone_proposal_id")
    private  CapstoneProposal capstoneProposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "council_id", nullable = false)
    private Council council;

    @Column(nullable = false)
    private LocalDate defenseDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String room;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    private Integer defenseRound ; // Vòng bảo vệ (1, 2, ...)

    public enum ScheduleStatus {
        SCHEDULED, // Đã lên lịch, chờ diễn ra
        COMPLETED, // Đã diễn ra xong
        CANCELED   // Bị hủy
    }
}

