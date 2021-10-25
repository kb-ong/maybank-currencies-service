package io.maybank.currenciesservice.service.exp;

public class CurrenciesException extends RuntimeException {
    public CurrenciesException(String message, Throwable cause) {
        super(message, cause);
    }
}
