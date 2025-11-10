//package fpt.com.capstone.service;
//
//
//import fpt.com.capstone.model.CouncilReview;
//import fpt.com.capstone.repository.CouncilReviewRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CouncilReviewService {
//
//    private final CouncilReviewRepository councilReviewRepository;
//
//    public List<CouncilReview> getAll() {
//        return councilReviewRepository.findAll();
//    }
//
//
//    public CouncilReview save(CouncilReview councilReview) {
//        return councilReviewRepository.save(councilReview);
//    }
//
//    public CouncilReview findById(int id) {
//        return councilReviewRepository.findById(id).orElse(null);
//    }
//
//    public CouncilReview update(CouncilReview councilReview) {
//        return councilReviewRepository.save(councilReview);
//    }
//}
