package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.customer.*;
import org.example.trangsuc_js.entity.CustomerAddress;
import org.example.trangsuc_js.entity.User;
import org.example.trangsuc_js.repository.CustomerAddressRepository;
import org.example.trangsuc_js.repository.UserRepository;
import org.example.trangsuc_js.service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    
    private final UserRepository userRepo;
    private final CustomerAddressRepository addressRepo;
    private final PasswordEncoder passwordEncoder;
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public CustomerProfileDto getProfile() {
        User user = getCurrentUser();
        return toCustomerProfileDto(user);
    }
    
    @Override
    @Transactional
    public CustomerProfileDto updateProfile(UpdateProfileDto dto) {
        User user = getCurrentUser();
        
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        
        if (dto.getDateOfBirth() != null) {
            user.setDateOfBirth(LocalDateTime.parse(dto.getDateOfBirth() + "T00:00:00"));
        }
        
        if (dto.getGender() != null) {
            user.setGender(User.Gender.valueOf(dto.getGender().toUpperCase()));
        }
        
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setProvince(dto.getProvince());
        user.setPostalCode(dto.getPostalCode());
        user.setCountry(dto.getCountry());
        user.setPreferredLanguage(dto.getPreferredLanguage());
        user.setPreferredCurrency(dto.getPreferredCurrency());
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepo.save(user);
        return toCustomerProfileDto(user);
    }
    
    @Override
    @Transactional
    public void changePassword(ChangePasswordDto dto) {
        User user = getCurrentUser();
        
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }
        
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }
        
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);
    }
    
    @Override
    @Transactional
    public String uploadAvatar(MultipartFile file) {
        User user = getCurrentUser();
        
        try {
            // For now, simulate file upload by creating a URL
            // In production, you would save the file to storage (AWS S3, local filesystem, etc.)
            String fileName = file.getOriginalFilename();
            String avatarUrl = "/uploads/avatars/" + user.getId() + "/" + fileName;
            
            user.setAvatarUrl(avatarUrl);
            user.setUpdatedAt(LocalDateTime.now());
            userRepo.save(user);
            
            return avatarUrl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload avatar: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AddressDto> getAddresses() {
        User user = getCurrentUser();
        return addressRepo.findByUser(user).stream()
                .map(this::toAddressDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public AddressDto addAddress(AddressDto dto) {
        User user = getCurrentUser();
        
        CustomerAddress address = new CustomerAddress();
        address.setUser(user);
        address.setType(CustomerAddress.AddressType.valueOf(dto.getType().name()));
        address.setFullName(dto.getFullName());
        address.setPhone(dto.getPhone());
        address.setAddress(dto.getAddress());
        address.setCity(dto.getCity());
        address.setProvince(dto.getProvince());
        address.setPostalCode(dto.getPostalCode());
        address.setIsDefault(dto.getIsDefault());
        
        // If this is set as default, unset other default addresses
        if (dto.getIsDefault()) {
            addressRepo.findByUserIdAndIsDefaultTrue(user.getId())
                    .ifPresent(addr -> {
                        addr.setIsDefault(false);
                        addressRepo.save(addr);
                    });
        }
        
        addressRepo.save(address);
        return toAddressDto(address);
    }
    
    @Override
    @Transactional
    public AddressDto updateAddress(Long id, AddressDto dto) {
        User user = getCurrentUser();
        CustomerAddress address = addressRepo.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        address.setType(CustomerAddress.AddressType.valueOf(dto.getType().name()));
        address.setFullName(dto.getFullName());
        address.setPhone(dto.getPhone());
        address.setAddress(dto.getAddress());
        address.setCity(dto.getCity());
        address.setProvince(dto.getProvince());
        address.setPostalCode(dto.getPostalCode());
        address.setIsDefault(dto.getIsDefault());
        address.setUpdatedAt(LocalDateTime.now());
        
        // If this is set as default, unset other default addresses
        if (dto.getIsDefault()) {
            addressRepo.findByUserIdAndIsDefaultTrue(user.getId())
                    .ifPresent(addr -> {
                        if (!addr.getId().equals(id)) {
                            addr.setIsDefault(false);
                            addressRepo.save(addr);
                        }
                    });
        }
        
        addressRepo.save(address);
        return toAddressDto(address);
    }
    
    @Override
    @Transactional
    public void deleteAddress(Long id) {
        User user = getCurrentUser();
        CustomerAddress address = addressRepo.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        addressRepo.delete(address);
    }
    
    @Override
    @Transactional
    public AddressDto setDefaultAddress(Long id) {
        User user = getCurrentUser();
        CustomerAddress address = addressRepo.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        // Unset current default
        addressRepo.findByUserIdAndIsDefaultTrue(user.getId())
                .ifPresent(addr -> {
                    addr.setIsDefault(false);
                    addressRepo.save(addr);
                });
        
        // Set new default
        address.setIsDefault(true);
        address.setUpdatedAt(LocalDateTime.now());
        addressRepo.save(address);
        
        return toAddressDto(address);
    }
    
    private CustomerProfileDto toCustomerProfileDto(User user) {
        CustomerProfileDto dto = new CustomerProfileDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender() != null ? user.getGender().name() : null);
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setProvince(user.getProvince());
        dto.setPostalCode(user.getPostalCode());
        dto.setCountry(user.getCountry());
        dto.setPreferredLanguage(user.getPreferredLanguage());
        dto.setPreferredCurrency(user.getPreferredCurrency());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setPhoneVerified(user.getPhoneVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setLastLoginAt(user.getLastLoginAt());
        return dto;
    }
    
    private AddressDto toAddressDto(CustomerAddress address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setType(AddressDto.AddressType.valueOf(address.getType().name()));
        dto.setFullName(address.getFullName());
        dto.setPhone(address.getPhone());
        dto.setAddress(address.getAddress());
        dto.setCity(address.getCity());
        dto.setProvince(address.getProvince());
        dto.setPostalCode(address.getPostalCode());
        dto.setIsDefault(address.getIsDefault());
        return dto;
    }
}
