package fpt.com.capstone.controller;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.service.CapstoneProposalService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;

@RestController
@RequestMapping("/api/capstone-proposal")
@CrossOrigin("*")
public class CapstoneProposalController {
    @Autowired
    private CapstoneProposalService capstoneProposalService;

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
    public CapstoneProposal reviewProposal(@RequestParam int proposalId, @RequestParam Boolean isApproved, @RequestParam String reason, @RequestParam int adminId) {

        return capstoneProposalService.reviewProposal( proposalId, isApproved, reason, adminId);
    }



    @GetMapping("{id}")
    public ResponseEntity<CapstoneProposal> findById( @PathVariable int id) {
        return ResponseEntity.ok(capstoneProposalService.findById(id));
    }
}
