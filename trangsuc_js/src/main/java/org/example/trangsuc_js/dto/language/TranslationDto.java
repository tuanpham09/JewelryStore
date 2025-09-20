package org.example.trangsuc_js.dto.language;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationDto {
    private String key;
    private String value;
    private String language;
    private String category;
}
