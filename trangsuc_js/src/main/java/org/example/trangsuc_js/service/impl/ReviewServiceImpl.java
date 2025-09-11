package org.example.trangsuc_js.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.review.ReviewDto;
import org.example.trangsuc_js.dto.review.ReviewResponse;
import org.example.trangsuc_js.entity.*;
import org.example.trangsuc_js.repository.*;
import org.example.trangsuc_js.service.ReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final ReviewRepository reviewRepo;

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).orElseThrow();
    }

    @Override
    public void addReview(ReviewDto dto) {
        User user = getCurrentUser();
        Product product = productRepo.findById(dto.getProductId()).orElseThrow();

        // kiểm tra user đã mua và đơn hàng đã DELIVERED
        boolean purchased = orderRepo.findByUser(user).stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .flatMap(o -> o.getItems().stream())
                .anyMatch(oi -> oi.getProduct().getId().equals(product.getId()));

        if (!purchased) {
            throw new RuntimeException("Bạn chỉ có thể đánh giá sản phẩm đã mua và đã nhận hàng");
        }

        Review review = reviewRepo.findByUserAndProduct(user, product).orElse(new Review());
        review.setUser(user);
        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        reviewRepo.save(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow();
        return reviewRepo.findByProduct(product).stream()
                .map(r -> new ReviewResponse(
                        r.getId(),
                        r.getUser().getFullName(),
                        r.getRating(),
                        r.getComment(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
