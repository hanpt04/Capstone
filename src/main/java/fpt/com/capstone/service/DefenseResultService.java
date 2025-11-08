package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.CreateDefenseResultRequest;
import fpt.com.capstone.model.DefenseResult;

import java.util.List;

public interface DefenseResultService {
    DefenseResult createDefenseResult(CreateDefenseResultRequest request);
    List<DefenseResult> getAllDefenseResults();
    List<DefenseResult> getDefenseResultsByProposalId(int proposalId);
}
