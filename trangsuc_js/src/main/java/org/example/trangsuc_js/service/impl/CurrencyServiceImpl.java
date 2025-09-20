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
    
    // Mock exchange rates (in production, these would come from an external API)
    private final Map<String, BigDecimal> exchangeRates = new HashMap<String, BigDecimal>() {{
        put("VND", BigDecimal.ONE);
        put("USD", BigDecimal.valueOf(24000));
        put("EUR", BigDecimal.valueOf(26000));
        put("CNY", BigDecimal.valueOf(3300));
        put("JPY", BigDecimal.valueOf(160));
    }};
    
    @Override
    @Transactional(readOnly = true)
    public List<CurrencyDto> getAvailableCurrencies() {
        return Arrays.asList(
            new CurrencyDto("VND", "Vietnamese Dong", "₫", "🇻🇳", BigDecimal.ONE, true, true),
            new CurrencyDto("USD", "US Dollar", "$", "🇺🇸", BigDecimal.valueOf(24000), true, false),
            new CurrencyDto("EUR", "Euro", "€", "🇪🇺", BigDecimal.valueOf(26000), true, false),
            new CurrencyDto("CNY", "Chinese Yuan", "¥", "🇨🇳", BigDecimal.valueOf(3300), true, false),
            new CurrencyDto("JPY", "Japanese Yen", "¥", "🇯🇵", BigDecimal.valueOf(160), true, false)
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
        BigDecimal fromRate = exchangeRates.get(fromCurrency);
        BigDecimal toRate = exchangeRates.get(toCurrency);
        
        if (fromRate == null || toRate == null) {
            throw new RuntimeException("Unsupported currency");
        }
        
        // Convert to VND first, then to target currency
        BigDecimal amountInVND = amount.multiply(fromRate);
        BigDecimal convertedAmount = amountInVND.divide(toRate, 2, RoundingMode.HALF_UP);
        BigDecimal exchangeRate = fromRate.divide(toRate, 4, RoundingMode.HALF_UP);
        
        return new CurrencyConversionDto(
            amount,
            fromCurrency,
            toCurrency,
            convertedAmount,
            exchangeRate,
            formatPrice(amount, fromCurrency),
            formatPrice(convertedAmount, toCurrency)
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
        BigDecimal fromRate = exchangeRates.get(fromCurrency);
        BigDecimal toRate = exchangeRates.get(toCurrency);
        
        if (fromRate == null || toRate == null) {
            throw new RuntimeException("Unsupported currency");
        }
        
        return fromRate.divide(toRate, 4, RoundingMode.HALF_UP);
    }
    
    @Override
    @Transactional
    public void updateExchangeRates() {
        // In production, this would fetch real-time exchange rates from an external API
        // For now, we'll just update the mock rates
        exchangeRates.put("USD", BigDecimal.valueOf(24000 + (Math.random() - 0.5) * 1000));
        exchangeRates.put("EUR", BigDecimal.valueOf(26000 + (Math.random() - 0.5) * 1000));
        exchangeRates.put("CNY", BigDecimal.valueOf(3300 + (Math.random() - 0.5) * 100));
        exchangeRates.put("JPY", BigDecimal.valueOf(160 + (Math.random() - 0.5) * 10));
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
