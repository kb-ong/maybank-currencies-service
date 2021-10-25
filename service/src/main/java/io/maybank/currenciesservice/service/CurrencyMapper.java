package io.maybank.currenciesservice.service;

import io.maybank.currenciesservice.dto.CurrencyDTO;
import io.maybank.currenciesservice.model.Currency;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CurrencyMapper {

    public abstract Currency mapToCurrency(CurrencyDTO currencyDTO);

    public abstract List<Currency> mapToCurrencyList(List<CurrencyDTO> currencyDTOList);

    public abstract List<CurrencyDTO> mapToCurrencyDTOList(List<Currency> CurrencyList);

    public abstract CurrencyDTO mapToCurrencyDTO(Currency currency);
}