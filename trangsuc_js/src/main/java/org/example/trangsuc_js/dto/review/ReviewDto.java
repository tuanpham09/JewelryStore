package org.example.trangsuc_js.dto.review;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewDto {
    private Long productId;
    private int rating;
    private String comment;
}
