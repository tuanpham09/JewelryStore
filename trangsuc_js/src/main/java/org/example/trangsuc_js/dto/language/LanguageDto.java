package org.example.trangsuc_js.dto.language;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {
    private String code;
    private String name;
    private String nativeName;
    private String flag;
    private Boolean isActive;
    private Boolean isDefault;
}
