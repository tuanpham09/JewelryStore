package org.example.trangsuc_js.dto.product;


import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter @Setter
public class ProductUpsertDto {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;
    
    @NotBlank(message = "Slug không được để trống")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug chỉ được chứa chữ thường, số và dấu gạch ngang")
    private String slug;
    
    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;
    
    @Size(max = 500, message = "Mô tả ngắn không được vượt quá 500 ký tự")
    private String shortDescription;
    
    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Giá gốc phải lớn hơn hoặc bằng 0")
    private BigDecimal originalPrice;
    
    @DecimalMin(value = "0.0", message = "Giá khuyến mãi phải lớn hơn hoặc bằng 0")
    private BigDecimal salePrice;
    
    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho phải lớn hơn hoặc bằng 0")
    private Integer stock;
    
    @Min(value = 0, message = "Số lượng tồn kho tối thiểu phải lớn hơn hoặc bằng 0")
    private Integer minStock;
    
    @Min(value = 1, message = "Số lượng tồn kho tối đa phải lớn hơn 0")
    private Integer maxStock;
    
    @Size(max = 100, message = "SKU không được vượt quá 100 ký tự")
    private String sku;
    
    @Size(max = 100, message = "Barcode không được vượt quá 100 ký tự")
    private String barcode;
    
    @DecimalMin(value = "0.0", message = "Trọng lượng phải lớn hơn hoặc bằng 0")
    private BigDecimal weight;
    
    @Size(max = 100, message = "Kích thước không được vượt quá 100 ký tự")
    private String dimensions;
    
    @Size(max = 100, message = "Chất liệu không được vượt quá 100 ký tự")
    private String material;
    
    @Size(max = 50, message = "Màu sắc không được vượt quá 50 ký tự")
    private String color;
    
    @Size(max = 100, message = "Thương hiệu không được vượt quá 100 ký tự")
    private String brand;
    
    @Size(max = 100, message = "Xuất xứ không được vượt quá 100 ký tự")
    private String origin;
    
    @Min(value = 0, message = "Thời gian bảo hành phải lớn hơn hoặc bằng 0")
    private Integer warrantyPeriod;
    
    private Boolean isActive = true;
    private Boolean isFeatured = false;
    private Boolean isNew = false;
    private Boolean isBestseller = false;
    
    @Size(max = 255, message = "Meta title không được vượt quá 255 ký tự")
    private String metaTitle;
    
    @Size(max = 500, message = "Meta description không được vượt quá 500 ký tự")
    private String metaDescription;
    
    @Size(max = 500, message = "Meta keywords không được vượt quá 500 ký tự")
    private String metaKeywords;
    
    // Legacy field
    @Size(max = 500, message = "URL ảnh thumbnail không được vượt quá 500 ký tự")
    private String thumbnail;
    
    // Category
    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;
}
