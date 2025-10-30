package fpt.com.capstone.controller;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.service.CapstoneProposalService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capstone-proposal")
public class CapstoneProposalController {
    @Autowired
    private CapstoneProposalService capstoneProposalService;

    @GetMapping
    public ResponseEntity<List<CapstoneProposal>> findAll() {
        List<CapstoneProposal> proposals = capstoneProposalService.getAll();
        return ResponseEntity.ok(proposals);
    }
    @PostMapping
    public ResponseEntity<CapstoneProposal> createProposal(@RequestBody CapstoneProposal proposal) {
        CapstoneProposal createdProposal = capstoneProposalService.save(proposal);
        return ResponseEntity.ok(createdProposal);
    }
    @GetMapping("{id}")
    public ResponseEntity<CapstoneProposal> findById( @PathVariable int id) {
        return ResponseEntity.ok(capstoneProposalService.findById(id));
    }
}
