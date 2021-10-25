package io.maybank.currenciesservice.service.impl;

import io.maybank.currenciesservice.dto.CurrencyDTO;
import io.maybank.currenciesservice.model.Currency;
import io.maybank.currenciesservice.persistence.CurrenciesRepository;
import io.maybank.currenciesservice.service.CurrenciesService;
import io.maybank.currenciesservice.service.CurrencyMapper;
import io.maybank.currenciesservice.service.exp.CurrenciesException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrenciesServiceImpl implements CurrenciesService {

    private final CurrenciesRepository currenciesRepository;
    private final CurrencyMapper currencyMapper;
    private final static int PAGE_SIZE = 10;

    @Override
    @Transactional
    public void createCurrency(CurrencyDTO currencyDTO) {
        Currency currency = currencyMapper.mapToCurrency(currencyDTO);
        currenciesRepository.save(currency);
    }

    @Override
    @Transactional
    @CacheEvict(value = "currencies", allEntries = true)
    public void updateCurrency(CurrencyDTO currencyDTO) {
        Currency currency = currenciesRepository.findByCode(currencyDTO.getCode());
        if (currency != null) {
            currency.setExchangeRate(currencyDTO.getExchangeRate());
            currency.setName(currencyDTO.getName());
            currenciesRepository.save(currency);
        } else {
            String emsg = String.format("Currency code:%s not found", currencyDTO.getCode());
            throw new CurrenciesException(emsg, null);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "currencies", allEntries = true)
    public void deleteCurrency(String code) {
        Currency currency = currenciesRepository.findByCode(code);
        if (currency == null) {
            String emsg = String.format("Currency code:%s not found", code);
            throw new CurrenciesException(emsg, null);
        }
        currenciesRepository.delete(currency);
    }

    @Override
    @Transactional
    @Cacheable("currencies")
    public CurrencyDTO getCurrencyByCode(String code) {
        Currency currency = currenciesRepository.findByCode(code);
        if (currency == null) {
            String emsg = String.format("Currency code:%s not found", code);
            throw new CurrenciesException(emsg, null);
        }
        return currencyMapper.mapToCurrencyDTO(currency);
    }

    @Override
    @Transactional
    @Cacheable("currencies")
    public List<CurrencyDTO> getAllCurrencies() {
        return currencyMapper.mapToCurrencyDTOList(currenciesRepository.findAll());
    }

    @Override
    @Transactional
    @Cacheable("currencies")
    public List<CurrencyDTO> getCurrenciesByPagination(int page) {
        return currencyMapper.mapToCurrencyDTOList(
                currenciesRepository.getCurrenciesByPagination(PAGE_SIZE, (page - 1) * PAGE_SIZE));
    }

}
