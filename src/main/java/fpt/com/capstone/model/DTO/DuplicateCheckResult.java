package fpt.com.capstone.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateCheckResult {

    boolean isDuplicate;
    Double distance; // Khoảng cách (càng nhỏ càng giống)
    String closestId ;// ID của proposal giống nhất (ví dụ: "proposal_123")
    int currtentId; // ID của proposal hiện tại (ví dụ: "proposal_456")

    public DuplicateCheckResult(boolean isDuplicate, Double distance, String closestId) {
        this.isDuplicate = isDuplicate;
        this.distance = distance;
        this.closestId = closestId;
    }
}
