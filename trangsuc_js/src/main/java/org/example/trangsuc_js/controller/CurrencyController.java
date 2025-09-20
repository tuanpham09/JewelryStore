package org.example.trangsuc_js.controller;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.currency.CurrencyConversionDto;
import org.example.trangsuc_js.dto.currency.CurrencyDto;
import org.example.trangsuc_js.service.CurrencyService;
import org.example.trangsuc_js.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    
    private final CurrencyService currencyService;
    
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<CurrencyDto>>> getAvailableCurrencies() {
        List<CurrencyDto> currencies = currencyService.getAvailableCurrencies();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Available currencies retrieved successfully",
            currencies
        ));
    }
    
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<CurrencyDto>> getCurrentCurrency() {
        CurrencyDto currency = currencyService.getCurrentCurrency();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Current currency retrieved successfully",
            currency
        ));
    }
    
    @PostMapping("/set/{currencyCode}")
    public ResponseEntity<ApiResponse<String>> setCurrentCurrency(@PathVariable String currencyCode) {
        currencyService.setCurrentCurrency(currencyCode);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Currency changed successfully",
            currencyCode
        ));
    }
    
    @GetMapping("/convert")
    public ResponseEntity<ApiResponse<CurrencyConversionDto>> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {
        CurrencyConversionDto conversion = currencyService.convertCurrency(amount, fromCurrency, toCurrency);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Currency converted successfully",
            conversion
        ));
    }
    
    @GetMapping("/format")
    public ResponseEntity<ApiResponse<String>> formatPrice(
            @RequestParam BigDecimal amount,
            @RequestParam String currencyCode) {
        String formattedPrice = currencyService.formatPrice(amount, currencyCode);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Price formatted successfully",
            formattedPrice
        ));
    }
    
    @GetMapping("/rate")
    public ResponseEntity<ApiResponse<BigDecimal>> getExchangeRate(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {
        BigDecimal rate = currencyService.getExchangeRate(fromCurrency, toCurrency);
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Exchange rate retrieved successfully",
            rate
        ));
    }
    
    @PostMapping("/update-rates")
    public ResponseEntity<ApiResponse<String>> updateExchangeRates() {
        currencyService.updateExchangeRates();
        return ResponseEntity.ok(new ApiResponse<>(
            true,
            "Exchange rates updated successfully",
            null
        ));
    }
}
