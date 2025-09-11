package org.example.trangsuc_js.dto.review;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String userName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
