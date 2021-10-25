package io.maybank.currenciesservice.controller;

import io.maybank.currenciesservice.dto.ErrorDTO;
import io.maybank.currenciesservice.dto.ErrorResponseDTO;
import io.maybank.currenciesservice.model.Currency;
import io.maybank.currenciesservice.service.exp.CurrenciesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            ResponseStatusException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            CurrenciesException.class
    })
    public ResponseEntity<ErrorResponseDTO> badRequest(Exception ex) {
        if (ex.getMessage() == null) {
            logger.warn("400 - Bad Request", ex);
            var errorDTO = new ErrorDTO("400", "Bad Request");
            var errorResponseDTO = new ErrorResponseDTO(errorDTO);
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        } else {
            ErrorDTO errorDTO;
            logger.warn("400 - Bad Request", ex);
            errorDTO = new ErrorDTO("400", ex.getMessage());
            var errorResponseDTO = new ErrorResponseDTO(errorDTO);
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> internalServerError(Exception ex) {
        logger.error("500 - Internal Server Error", ex);
        String errorMessage;
        if (ex.getMessage() == null) {
            errorMessage = "Internal Server Error";
        } else {
            errorMessage = ex.getMessage();
        }
        var errorDTO = new ErrorDTO("500", errorMessage);
        var errorResponseDTO = new ErrorResponseDTO(errorDTO);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
