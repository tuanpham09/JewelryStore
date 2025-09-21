package org.example.trangsuc_js.dto.customer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDto {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String avatarUrl;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String preferredLanguage;
    private String preferredCurrency;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}
