package org.example.trangsuc_js.controller;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.review.ReviewDto;
import org.example.trangsuc_js.dto.review.ReviewResponse;
import org.example.trangsuc_js.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public String addReview(@RequestBody ReviewDto dto) {
        reviewService.addReview(dto);
        return "Review added successfully";
    }

    @GetMapping("/product/{productId}")
    public List<ReviewResponse> getReviews(@PathVariable Long productId) {
        return reviewService.getReviewsByProduct(productId);
    }
}
