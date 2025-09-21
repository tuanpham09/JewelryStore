package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.customer.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    // Profile Management
    CustomerProfileDto getProfile();
    CustomerProfileDto updateProfile(UpdateProfileDto dto);
    void changePassword(ChangePasswordDto dto);
    String uploadAvatar(MultipartFile file);
    
    // Address Management
    List<AddressDto> getAddresses();
    AddressDto addAddress(AddressDto dto);
    AddressDto updateAddress(Long id, AddressDto dto);
    void deleteAddress(Long id);
    AddressDto setDefaultAddress(Long id);
}
