package fpt.com.capstone.mapper;


import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.CapstoneProposalHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapstoneProposalHistoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "capstoneProposal", source = "proposal")
    @Mapping(target = "reason", source = "reason")
    @Mapping(target = "createdAt", ignore = true)
    CapstoneProposalHistory toHistory(CapstoneProposal proposal, String reason);
}
