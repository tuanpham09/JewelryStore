package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.currency.CurrencyConversionDto;
import org.example.trangsuc_js.dto.currency.CurrencyDto;
import org.example.trangsuc_js.service.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    
    private String currentCurrency = "VND";
    
    // Bỏ exchange rates - giữ nguyên giá gốc
    private final Map<String, BigDecimal> exchangeRates = new HashMap<String, BigDecimal>() {{
        put("VND", BigDecimal.ONE);
        put("USD", BigDecimal.ONE); // Tỷ giá = 1
        put("EUR", BigDecimal.ONE); // Tỷ giá = 1
        put("CNY", BigDecimal.ONE); // Tỷ giá = 1
        put("JPY", BigDecimal.ONE); // Tỷ giá = 1
    }};
    
    @Override
    @Transactional(readOnly = true)
    public List<CurrencyDto> getAvailableCurrencies() {
        return Arrays.asList(
            new CurrencyDto("VND", "Vietnamese Dong", "₫", "🇻🇳", BigDecimal.ONE, true, true),
            new CurrencyDto("USD", "US Dollar", "$", "🇺🇸", BigDecimal.ONE, true, false), // Tỷ giá = 1
            new CurrencyDto("EUR", "Euro", "€", "🇪🇺", BigDecimal.ONE, true, false), // Tỷ giá = 1
            new CurrencyDto("CNY", "Chinese Yuan", "¥", "🇨🇳", BigDecimal.ONE, true, false), // Tỷ giá = 1
            new CurrencyDto("JPY", "Japanese Yen", "¥", "🇯🇵", BigDecimal.ONE, true, false) // Tỷ giá = 1
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public CurrencyDto getCurrentCurrency() {
        return getAvailableCurrencies().stream()
                .filter(currency -> currency.getCode().equals(currentCurrency))
                .findFirst()
                .orElse(getAvailableCurrencies().get(0));
    }
    
    @Override
    @Transactional
    public void setCurrentCurrency(String currencyCode) {
        this.currentCurrency = currencyCode;
    }
    
    @Override
    @Transactional(readOnly = true)
    public CurrencyConversionDto convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        // Bỏ logic chuyển đổi tiền tệ - giữ nguyên giá gốc
        return new CurrencyConversionDto(
            amount,
            fromCurrency,
            toCurrency,
            amount, // Giữ nguyên giá gốc
            BigDecimal.ONE, // Tỷ giá = 1
            formatPrice(amount, fromCurrency),
            formatPrice(amount, toCurrency)
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public String formatPrice(BigDecimal amount, String currencyCode) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        
        String symbol = getCurrencySymbol(currencyCode);
        String formattedAmount = formatter.format(amount);
        
        switch (currencyCode) {
            case "VND":
                return formattedAmount + " ₫";
            case "USD":
                return "$" + formattedAmount;
            case "EUR":
                return "€" + formattedAmount;
            case "CNY":
            case "JPY":
                return "¥" + formattedAmount;
            default:
                return symbol + formattedAmount;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        // Bỏ logic chuyển đổi tiền tệ - tỷ giá luôn = 1
        return BigDecimal.ONE;
    }
    
    @Override
    @Transactional
    public void updateExchangeRates() {
        // Bỏ logic cập nhật tỷ giá - giữ nguyên tỷ giá = 1
        // Không làm gì cả để tránh thay đổi giá sản phẩm
    }
    
    private String getCurrencySymbol(String currencyCode) {
        switch (currencyCode) {
            case "VND":
                return "₫";
            case "USD":
                return "$";
            case "EUR":
                return "€";
            case "CNY":
            case "JPY":
                return "¥";
            default:
                return currencyCode;
        }
    }
}
