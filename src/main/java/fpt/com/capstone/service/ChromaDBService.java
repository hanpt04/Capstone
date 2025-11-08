package fpt.com.capstone.service;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.DTO.DuplicateCheckResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface ChromaDBService {
    boolean uploadProposal(CapstoneProposal proposal);
    DuplicateCheckResult checkDuplicate(CapstoneProposal proposal);
    void deleteProposal(Integer proposalId);
    boolean checkConnection();
}
