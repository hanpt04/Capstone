package fpt.com.capstone.service;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.repository.CapstoneProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CapstoneProposalService {
    @Autowired
    private CapstoneProposalRepository capstoneProposalRepository;

    public List<CapstoneProposal> getAll(){
        return capstoneProposalRepository.findAll();
    }

    public CapstoneProposal save (CapstoneProposal proposal){
        return capstoneProposalRepository.save(proposal);
    }
    public CapstoneProposal findById(int id){
        return capstoneProposalRepository.findById(id).orElse(null);
    }

}
