package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.language.LanguageDto;
import org.example.trangsuc_js.service.LanguageService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/language")
@RequiredArgsConstructor
public class LanguageController {
    
    private final LanguageService languageService;
    
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<LanguageDto>>> getAvailableLanguages() {
        List<LanguageDto> languages = languageService.getAvailableLanguages();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Available languages retrieved successfully",
            languages
        ));
    }
    
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<LanguageDto>> getCurrentLanguage() {
        LanguageDto language = languageService.getCurrentLanguage();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Current language retrieved successfully",
            language
        ));
    }
    
    @PostMapping("/set/{languageCode}")
    public ResponseEntity<ApiResponse<String>> setCurrentLanguage(@PathVariable String languageCode) {
        languageService.setCurrentLanguage(languageCode);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Language changed successfully",
            languageCode
        ));
    }
    
    @GetMapping("/translations/{languageCode}")
    public ResponseEntity<ApiResponse<Map<String, String>>> getTranslations(@PathVariable String languageCode) {
        Map<String, String> translations = languageService.getTranslations(languageCode);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Translations retrieved successfully",
            translations
        ));
    }
    
    @GetMapping("/translations/{languageCode}/{category}")
    public ResponseEntity<ApiResponse<Map<String, String>>> getTranslationsByCategory(
            @PathVariable String languageCode,
            @PathVariable String category) {
        Map<String, String> translations = languageService.getTranslationsByCategory(languageCode, category);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Category translations retrieved successfully",
            translations
        ));
    }
    
    @GetMapping("/translate/{key}")
    public ResponseEntity<ApiResponse<String>> translate(@PathVariable String key) {
        String translation = languageService.translate(key);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Translation retrieved successfully",
            translation
        ));
    }
    
    @GetMapping("/translate/{key}/{languageCode}")
    public ResponseEntity<ApiResponse<String>> translateWithLanguage(
            @PathVariable String key,
            @PathVariable String languageCode) {
        String translation = languageService.translate(key, languageCode);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Translation retrieved successfully",
            translation
        ));
    }
}
