package io.maybank.currenciesservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Schema(name = "Currency")
@EqualsAndHashCode(of = "code")
public class CurrencyDTO {
    @NotEmpty
    private String code;

    @NotEmpty
    private String name;

    private double exchangeRate;

    public CurrencyDTO(String code,String name,double exchangeRate){
        this.code=code;
        this.name=name;
        this.exchangeRate=exchangeRate;
    }

    public CurrencyDTO(){

    }

}
