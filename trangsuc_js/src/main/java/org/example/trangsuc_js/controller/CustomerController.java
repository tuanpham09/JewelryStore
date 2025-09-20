package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.customer.*;
import org.example.trangsuc_js.service.CustomerService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    
    // ========== PROFILE MANAGEMENT ==========
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<CustomerProfileDto>> getProfile() {
        CustomerProfileDto profile = customerService.getProfile();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Profile retrieved successfully",
            profile
        ));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<CustomerProfileDto>> updateProfile(
            @Valid @RequestBody UpdateProfileDto dto) {
        CustomerProfileDto profile = customerService.updateProfile(dto);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Profile updated successfully",
            profile
        ));
    }
    
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordDto dto) {
        customerService.changePassword(dto);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Password changed successfully",
            null
        ));
    }
    
    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<String>> uploadAvatar(
            @RequestParam("file") MultipartFile file) {
        String avatarUrl = customerService.uploadAvatar(file);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Avatar uploaded successfully",
            avatarUrl
        ));
    }
    
    // ========== ADDRESS MANAGEMENT ==========
    
    @GetMapping("/addresses")
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAddresses() {
        List<AddressDto> addresses = customerService.getAddresses();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Addresses retrieved successfully",
            addresses
        ));
    }
    
    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<AddressDto>> addAddress(
            @Valid @RequestBody AddressDto dto) {
        AddressDto address = customerService.addAddress(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
            true,
            "Address added successfully",
            address
        ));
    }
    
    @PutMapping("/addresses/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressDto dto) {
        AddressDto address = customerService.updateAddress(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Address updated successfully",
            address
        ));
    }
    
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
        customerService.deleteAddress(id);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Address deleted successfully",
            null
        ));
    }
    
    @PutMapping("/addresses/{id}/default")
    public ResponseEntity<ApiResponse<AddressDto>> setDefaultAddress(@PathVariable Long id) {
        AddressDto address = customerService.setDefaultAddress(id);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Default address set successfully",
            address
        ));
    }
}
