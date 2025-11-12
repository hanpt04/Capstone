package fpt.com.capstone.controller;


import fpt.com.capstone.exception.CustomException;
import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.repository.CapstoneProposalRepository;
import fpt.com.capstone.service.CapstoneProposalService;
import fpt.com.capstone.service.ChromaDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chromadb")
@RequiredArgsConstructor
@Slf4j
public class ChromaDBController {

    private final ChromaDBService chromaDBService;
    private final CapstoneProposalService proposalService;

    /**
     * Kiểm tra kết nối ChromaDB
     */
    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        boolean isConnected = chromaDBService.checkConnection();
        if (isConnected) {
            System.out.println("Calling Flask service health check...");
            return ResponseEntity.ok("ChromaDB is connected and running!");
        } else {
            return ResponseEntity.status(503).body("Cannot connect to ChromaDB");
        }
    }


    @PostMapping("/sync/{proposalId}")
    public ResponseEntity<String> syncProposal(@PathVariable Integer proposalId) {
        try {
            CapstoneProposal proposal = proposalService.findById(proposalId);
            if (proposal == null) {
                throw new CustomException("Proposal not found for id: " + proposalId, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            chromaDBService.uploadProposal(proposal);
            return ResponseEntity.ok("Synced proposal " + proposalId + " to ChromaDB");
        } catch (Exception e) {
            log.error("Error syncing proposal", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * Xóa một proposal khỏi ChromaDB
     */
    @DeleteMapping("/delete/{proposalId}")
    public ResponseEntity<String> deleteProposal(@PathVariable Integer proposalId) {
        try {
            chromaDBService.deleteProposal(proposalId);
            return ResponseEntity.ok("Deleted proposal " + proposalId + " from ChromaDB");
        } catch (Exception e) {
            log.error("Error deleting proposal", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}