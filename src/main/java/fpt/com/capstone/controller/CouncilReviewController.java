//package fpt.com.capstone.controller;
//
//import fpt.com.capstone.model.CouncilReview;
//import fpt.com.capstone.service.CouncilReviewService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping ("/api/council-review")
//@CrossOrigin ("*")
//public class CouncilReviewController {
//
//    private final CouncilReviewService councilReviewService;
//
//    @GetMapping
//    public List< CouncilReview> getAll() {
//        return councilReviewService.getAll();
//    }
//
//    @PostMapping
//    public CouncilReview createCouncilReview(@RequestBody CouncilReview councilReview) {
//        return councilReviewService.save(councilReview);
//    }
//
//    @GetMapping("/{id}")
//    public CouncilReview getCouncilReviewById(@PathVariable int id) {
//        return councilReviewService.findById(id);
//    }
//
//    @PutMapping
//    public CouncilReview updateCouncilReview(@RequestBody CouncilReview councilReview) {
//        return councilReviewService.update(councilReview);
//    }
//}
