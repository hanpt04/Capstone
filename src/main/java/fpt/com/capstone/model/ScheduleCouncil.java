package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleCouncil{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "schedule_id")
    @ManyToOne
    private Schedule schedule;
    @JoinColumn(name = "council_id")
    @ManyToOne
    private Council council;

    int proposalId;

    private int status;
    // 1 đã đc phân lịch
    // 2 hoàn thành
    // 3 cancel

}
