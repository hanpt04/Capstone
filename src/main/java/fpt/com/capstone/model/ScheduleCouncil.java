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

}
