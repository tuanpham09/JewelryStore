package org.example.trangsuc_js.dto.wishlist;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDto {
    private Long id;
    private Long userId;
    private List<WishlistItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
