package fpt.com.capstone.controller;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.repository.CapstoneProposalRepository;
import fpt.com.capstone.repository.LecturerRepository;
import fpt.com.capstone.service.ChromaDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final CapstoneProposalRepository proposalRepository;
    private final ChromaDBService chromaDBService;
    private final LecturerRepository lecturerRepository;
    /**
     * Nhận proposal và upload lên ChromaDB luôn
     */
    @PostMapping("/upload-proposal")
    public ResponseEntity<String> uploadProposal(@RequestBody CapstoneProposal proposal) {
        try {
            System.out.println("=== Step 1: Creating collection ===");
            System.out.println("=== Collection created/exists ===");

            System.out.println("=== Step 2: Saving to DB ===");
            System.out.println("Saved proposal with ID: " + proposal.getId());
            CapstoneProposal saved = proposalRepository.save( proposal);
            System.out.println("=== Step 3: Uploading to ChromaDB ===");
            System.out.println("Result: "+ chromaDBService.uploadProposal(saved));
            System.out.println("Uploaded proposal with ID: " + proposal.getId() + " to ChromaDB");

            return ResponseEntity.ok("Success! Proposal ID: " + proposal.getId() + " uploaded to ChromaDB");
        } catch (Exception e) {
            System.out.println("Error uploading proposal: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }




    @PostMapping
    public Lecturer createLecturer(Lecturer lecturer) {
        return lecturerRepository.save(lecturer);
    }


}