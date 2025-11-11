package fpt.com.capstone.controller;


import fpt.com.capstone.dto.request.CreateCouncilRequest;
import fpt.com.capstone.dto.request.UpdateCouncilRequest;
import fpt.com.capstone.dto.response.CouncilResponse;
import fpt.com.capstone.model.Council;
import fpt.com.capstone.service.CouncilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/councils")
public class CouncilController {

    @Autowired
    CouncilService councilService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Council createCouncil(@Valid @RequestBody CreateCouncilRequest request) {

        return councilService.createCouncil(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public List<CouncilResponse> getAllCouncils() {
        return councilService.getAllCouncils();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR')")
    public Council getCouncilById(@PathVariable Integer id) {
        return councilService.getCouncilById(id);
    }

    @PutMapping("/{councilId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Council updateCouncil(@PathVariable int councilId, @Valid @RequestBody UpdateCouncilRequest request) {
        return councilService.updateCouncil(councilId, request);

    }

}
