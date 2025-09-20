package org.example.trangsuc_js.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistoryDto {
    private String status;
    private String statusDescription;
    private String note;
    private LocalDateTime timestamp;
    private String updatedBy; // Admin name or system
}
