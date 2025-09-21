package org.example.trangsuc_js.dto.customer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    
    @NotNull(message = "Loại địa chỉ không được để trống")
    private AddressType type;
    
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 255, message = "Họ tên không được vượt quá 255 ký tự")
    private String fullName;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    private String phone;
    
    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;
    
    @NotBlank(message = "Thành phố không được để trống")
    @Size(max = 100, message = "Thành phố không được vượt quá 100 ký tự")
    private String city;
    
    @NotBlank(message = "Tỉnh/thành không được để trống")
    @Size(max = 100, message = "Tỉnh/thành không được vượt quá 100 ký tự")
    private String province;
    
    @Size(max = 20, message = "Mã bưu điện không được vượt quá 20 ký tự")
    private String postalCode;
    
    private Boolean isDefault = false;
    
    public enum AddressType {
        HOME, WORK, OTHER
    }
}
