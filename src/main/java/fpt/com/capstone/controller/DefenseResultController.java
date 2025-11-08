package fpt.com.capstone.controller;

import fpt.com.capstone.dto.request.CreateDefenseResultRequest;
import fpt.com.capstone.model.DefenseResult;
import fpt.com.capstone.service.DefenseResultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/defense-results")
public class DefenseResultController {

    @Autowired
    DefenseResultService defenseResultService;

    @PostMapping
    public DefenseResult createDefenseResult(@Valid @RequestBody CreateDefenseResultRequest request) {
        return defenseResultService.createDefenseResult(request);
    }

    @GetMapping
    public List< DefenseResult> getAllDefenseResults() {
        return defenseResultService.getAllDefenseResults();
    }

    @GetMapping("/proposal/{proposalId}")
    public List<DefenseResult> getDefenseResultsByProposalId(@PathVariable int proposalId) {
        return defenseResultService.getDefenseResultsByProposalId(proposalId);
    }
}
