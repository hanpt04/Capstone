package fpt.com.capstone.controller;


import fpt.com.capstone.dto.request.CreateCouncilRequest;
import fpt.com.capstone.dto.request.UpdateCouncilRequest;
import fpt.com.capstone.dto.response.CouncilResponse;
import fpt.com.capstone.model.Council;
import fpt.com.capstone.service.CouncilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/councils")
public class CouncilController {

    @Autowired
    CouncilService councilService;


    @PostMapping
    public Council createCouncil(@Valid @RequestBody CreateCouncilRequest request) {

        return councilService.createCouncil(request);
    }

    @GetMapping
    public List<CouncilResponse> getAllCouncils() {
        return councilService.getAllCouncils();
    }

    @GetMapping("/{id}")
    public Council getCouncilById(@PathVariable Integer id) {
        return councilService.getCouncilById(id);
    }

    @PutMapping("/{councilId}")
    public Council updateCouncil(@PathVariable int councilId, @Valid @RequestBody UpdateCouncilRequest request) {
        return councilService.updateCouncil(councilId, request);

    }

}
