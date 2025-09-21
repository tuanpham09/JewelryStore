package org.example.trangsuc_js.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String gender;
    private Boolean enabled;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private List<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private String avatarUrl;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String preferredLanguage;
    private String preferredCurrency;
    private Integer totalOrders;
    private Double totalSpent;
}
