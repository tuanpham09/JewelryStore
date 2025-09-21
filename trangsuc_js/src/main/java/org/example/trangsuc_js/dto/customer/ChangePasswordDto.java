package org.example.trangsuc_js.dto.customer;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ChangePasswordDto {
    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    private String currentPassword;
    
    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, message = "Mật khẩu mới phải có ít nhất 6 ký tự")
    private String newPassword;
    
    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
}
