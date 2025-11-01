package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
class Council {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    @JoinColumn(name = "semester_id")
    @ManyToOne
    private Semester semester;
    private int totalMembers;
    private int status; // 1:ACTIVE, 2:INACTIVE, 3:COMPLETED
    @JoinColumn(name = "schdule_council_id")
    @ManyToOne
    private ScheduleCouncil scheduleCouncil;

}
