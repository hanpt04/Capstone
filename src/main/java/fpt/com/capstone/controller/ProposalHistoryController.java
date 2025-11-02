package fpt.com.capstone.controller;

import fpt.com.capstone.model.CapstoneProposalHistory;
import fpt.com.capstone.service.ProposalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proposal-history")
public class ProposalHistoryController {
    @Autowired
    private ProposalHistoryService proposalHistoryService;

    @PostMapping
    public ResponseEntity<CapstoneProposalHistory> createHistory(CapstoneProposalHistory history) {
        CapstoneProposalHistory createdHistory = proposalHistoryService.save(history);
        return ResponseEntity.ok(createdHistory);
    }

    @GetMapping
    public ResponseEntity<List<CapstoneProposalHistory>> getAllHistories() {
        List<CapstoneProposalHistory> histories = proposalHistoryService.getAllProposalHistory();
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/{id}/history")
    public List< CapstoneProposalHistory> getProposalHistory  ( @PathVariable int id ) {
        return proposalHistoryService.getHistoriesByProposalId( id);
    }

    @GetMapping("{id}")
    public ResponseEntity<CapstoneProposalHistory> findById(@PathVariable int id) {
        return ResponseEntity.ok(proposalHistoryService.getProposalHistory(id));
    }
}
