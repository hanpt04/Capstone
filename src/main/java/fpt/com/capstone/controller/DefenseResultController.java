package fpt.com.capstone.controller;

import fpt.com.capstone.dto.request.CreateDefenseResultRequest;
import fpt.com.capstone.model.DefenseResult;
import fpt.com.capstone.service.DefenseResultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/defense-results")
public class DefenseResultController {

    @Autowired
    DefenseResultService defenseResultService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public DefenseResult createDefenseResult(@Valid @RequestBody CreateDefenseResultRequest request) {
        return defenseResultService.createDefenseResult(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public List< DefenseResult> getAllDefenseResults() {
        return defenseResultService.getAllDefenseResults();
    }

    @GetMapping("/proposal/{proposalId}")
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public List<DefenseResult> getDefenseResultsByProposalId(@PathVariable int proposalId) {
        return defenseResultService.getDefenseResultsByProposalId(proposalId);
    }
}
