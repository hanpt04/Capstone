package fpt.com.capstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
