package io.maybank.currenciesservice.service.exp;

public class Auth0Exception extends RuntimeException {
    public Auth0Exception(String message, Throwable cause) {
        super(message, cause);
    }
}