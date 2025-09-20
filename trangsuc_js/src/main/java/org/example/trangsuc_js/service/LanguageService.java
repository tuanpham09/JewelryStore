package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.language.LanguageDto;
import org.example.trangsuc_js.dto.language.TranslationDto;

import java.util.List;
import java.util.Map;

public interface LanguageService {
    List<LanguageDto> getAvailableLanguages();
    LanguageDto getCurrentLanguage();
    void setCurrentLanguage(String languageCode);
    Map<String, String> getTranslations(String languageCode);
    Map<String, String> getTranslationsByCategory(String languageCode, String category);
    String translate(String key, String languageCode);
    String translate(String key);
}
