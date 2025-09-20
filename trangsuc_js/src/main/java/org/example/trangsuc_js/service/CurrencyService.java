package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.currency.CurrencyConversionDto;
import org.example.trangsuc_js.dto.currency.CurrencyDto;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyService {
    List<CurrencyDto> getAvailableCurrencies();
    CurrencyDto getCurrentCurrency();
    void setCurrentCurrency(String currencyCode);
    CurrencyConversionDto convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency);
    String formatPrice(BigDecimal amount, String currencyCode);
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);
    void updateExchangeRates();
}
