package fpt.com.capstone.service;

import fpt.com.capstone.model.CapstoneProposal;

import java.time.LocalDateTime;
import java.util.List;

public interface CapstoneProposalService {
    List<CapstoneProposal> getAll();
    List<CapstoneProposal> getProposalsByReviewer(String lecturerCode);
    CapstoneProposal updateReview (int id, LocalDateTime date, int reviewTime, String  mentorCode1, String mentorCode2, String mentorName1, String mentorName2);
    CapstoneProposal save(CapstoneProposal proposal);
    CapstoneProposal reviewProposal(int proposalId, Boolean isApproved, String reason, int adminId);
    CapstoneProposal findById(int id);
    List<CapstoneProposal> getForAdminApprove(Integer adminId);
}
