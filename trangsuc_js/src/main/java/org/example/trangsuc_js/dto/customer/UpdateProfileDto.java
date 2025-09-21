package org.example.trangsuc_js.dto.customer;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class UpdateProfileDto {
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 255, message = "Họ tên không được vượt quá 255 ký tự")
    private String fullName;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
    private String phoneNumber;
    
    private String dateOfBirth;
    
    private String gender;
    
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;
    
    @Size(max = 100, message = "Thành phố không được vượt quá 100 ký tự")
    private String city;
    
    @Size(max = 100, message = "Tỉnh/thành không được vượt quá 100 ký tự")
    private String province;
    
    @Size(max = 20, message = "Mã bưu điện không được vượt quá 20 ký tự")
    private String postalCode;
    
    @Size(max = 100, message = "Quốc gia không được vượt quá 100 ký tự")
    private String country;
    
    @Pattern(regexp = "^(vi|en|zh|ja)$", message = "Ngôn ngữ không được hỗ trợ")
    private String preferredLanguage;
    
    @Pattern(regexp = "^(VND|USD|EUR|CNY)$", message = "Tiền tệ không được hỗ trợ")
    private String preferredCurrency;
}
