package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.CreateCouncilRequest;
import fpt.com.capstone.dto.request.UpdateCouncilRequest;
import fpt.com.capstone.dto.response.CouncilResponse;
import fpt.com.capstone.model.Council;

import java.util.List;

public interface CouncilService {
    Council createCouncil(CreateCouncilRequest request);
    List<CouncilResponse> getAllCouncils();
    Council getCouncilById(int id);
    Council updateCouncil(int councilId, UpdateCouncilRequest request);
}
