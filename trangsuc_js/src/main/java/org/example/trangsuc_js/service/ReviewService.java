package org.example.trangsuc_js.service;


import org.example.trangsuc_js.dto.review.ReviewDto;
import org.example.trangsuc_js.dto.review.ReviewResponse;

import java.util.List;

public interface ReviewService {
    void addReview(ReviewDto dto);
    List<ReviewResponse> getReviewsByProduct(Long productId);
}
