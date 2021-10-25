package io.maybank.currenciesservice.service;

import io.maybank.currenciesservice.dto.CurrencyDTO;

import java.util.List;

public interface CurrenciesService {
    void createCurrency(CurrencyDTO currencyDTO);

    void updateCurrency(CurrencyDTO currencyDTO);

    void deleteCurrency(String code);

    CurrencyDTO getCurrencyByCode(String code);

    List<CurrencyDTO> getAllCurrencies();

    List<CurrencyDTO> getCurrenciesByPagination(int page);
}
