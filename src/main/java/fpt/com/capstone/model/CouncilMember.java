package fpt.com.capstone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CouncilMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name  = "lecturer_id")
    @ManyToOne
    private Lecturer lecturer;

    @JoinColumn(name = "council_id")
    @ManyToOne
    private Council council;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouncilRole role;

    public enum CouncilRole {
        PRESIDENT,  // 1. Chủ tịch
        SECRETARY,  // 2. Thư ký
        REVIEWER,   // 3. Giám khảo (Bạn sẽ add 3 người với role này)
        GUEST       // 4. Khách mời (Optional)
    }
}
