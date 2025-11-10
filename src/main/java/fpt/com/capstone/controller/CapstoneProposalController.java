package fpt.com.capstone.controller;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.Ratio;
import fpt.com.capstone.repository.RatioRepository;
import fpt.com.capstone.service.CapstoneProposalService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/capstone-proposal")
@CrossOrigin("*")
public class CapstoneProposalController {
    @Autowired
    private CapstoneProposalService capstoneProposalService;
    @Autowired
    RatioRepository ratioRepository;

    @GetMapping
    public ResponseEntity<List<CapstoneProposal>> findAll() {
        List<CapstoneProposal> proposals = capstoneProposalService.getAll();
        return ResponseEntity.ok(proposals);
    }

    @PostMapping
    public CapstoneProposal createProposal(@RequestBody CapstoneProposal proposal) {
        return capstoneProposalService.save(proposal);
    }

    @PutMapping
    public CapstoneProposal reviewProposal(@RequestParam int proposalId, @RequestParam Boolean isApproved, @RequestParam String reason, @RequestParam String reviewerCode) {

        return capstoneProposalService.reviewProposal( proposalId, isApproved, reason, reviewerCode);
    }

    @PutMapping("/update-review")
    public CapstoneProposal updateReview(@RequestParam int proposalId,  @RequestParam LocalDateTime date ,@RequestParam int reviewTime, @RequestParam String mentorCode1, @RequestParam String mentorCode2, @RequestParam String mentorName1, @RequestParam String mentorName2) {
        return capstoneProposalService.updateReview(proposalId, date, reviewTime, mentorCode1, mentorCode2, mentorName1, mentorName2);
    }


    @GetMapping("/by-reviewer/{lecturerCode}")
    public List<CapstoneProposal> getProposalsByReviewer(@PathVariable String lecturerCode) {
        return capstoneProposalService.getProposalsByReviewer(lecturerCode);
    }

    @PutMapping("/rate")
    public Ratio updateRatio (@RequestBody Ratio ratio) {
        return ratioRepository.save(ratio);
    }

    @GetMapping("/rate")
    public Ratio getRatio () {
        return ratioRepository.findById(1).orElse(null);
    }



    @GetMapping("{id}")
    public ResponseEntity<CapstoneProposal> findById( @PathVariable int id) {
        return ResponseEntity.ok(capstoneProposalService.findById(id));
    }

//    @GetMapping("/by-admin/{adminId}")
//    public ResponseEntity<List<CapstoneProposal>> findByAdminId( @PathVariable Integer adminId) {
//        return ResponseEntity.ok(capstoneProposalService.getForAdminAprove(adminId));}
}
