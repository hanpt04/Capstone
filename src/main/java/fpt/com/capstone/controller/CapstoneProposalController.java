package fpt.com.capstone.controller;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.service.CapstoneProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/capstone-proposal")
@CrossOrigin("*")
public class CapstoneProposalController {
    @Autowired
    private CapstoneProposalService capstoneProposalService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public ResponseEntity<List<CapstoneProposal>> findAll() {
        List<CapstoneProposal> proposals = capstoneProposalService.getAll();
        return ResponseEntity.ok(proposals);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public CapstoneProposal createProposal(@RequestBody CapstoneProposal proposal) {
        proposal.setIsAdmin1(null);
        proposal.setIsAdmin2(null);
        return capstoneProposalService.save(proposal);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CapstoneProposal reviewProposal(@RequestParam int proposalId, @RequestParam Boolean isApproved, @RequestParam String reason, @RequestParam int adminId) {

        return capstoneProposalService.reviewProposal( proposalId, isApproved, reason, adminId);
    }

    @PutMapping("/update-review")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CapstoneProposal updateReview(@RequestParam int proposalId,  @RequestParam LocalDateTime date ,@RequestParam int reviewTime, @RequestParam String mentorCode1, @RequestParam String mentorCode2, @RequestParam String mentorName1, @RequestParam String mentorName2) {
        return capstoneProposalService.updateReview(proposalId, date, reviewTime, mentorCode1, mentorCode2, mentorName1, mentorName2);
    }


    @GetMapping("/by-reviewer/{lecturerCode}")
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public List<CapstoneProposal> getProposalsByReviewer(@PathVariable String lecturerCode) {
        return capstoneProposalService.getProposalsByReviewer(lecturerCode);
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public ResponseEntity<CapstoneProposal> findById( @PathVariable int id) {
        return ResponseEntity.ok(capstoneProposalService.findById(id));
    }

    @GetMapping("/by-admin/{adminId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<CapstoneProposal>> findByAdminId( @PathVariable Integer adminId) {
        return ResponseEntity.ok(capstoneProposalService.getForAdminApprove(adminId));}
}
