package io.maybank.currenciesservice.controller;

import io.maybank.currenciesservice.controller.configuration.ControllerConstants;
import io.maybank.currenciesservice.dto.CurrencyDTO;
import io.maybank.currenciesservice.dto.ErrorResponseDTO;
import io.maybank.currenciesservice.service.CurrenciesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping(ControllerConstants.V1_PATH_PREFIX + "/currencies")
@Tag(name = "Maybank: Currencies Service", description = "Maybank: Currencies Service Controller")
@RequiredArgsConstructor
public class CurrenciesController extends BaseController {

    private final CurrenciesService currenciesService;

    @Operation(description = "get all currencies by pagination.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @GetMapping(value = "/getPagination", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCurrenciesByPage(@RequestParam @javax.validation.constraints.Min(1) int page) {
        return new ResponseEntity<>(currenciesService.getCurrenciesByPagination(page), HttpStatus.OK);
    }

    @Operation(description = "get all currencies.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllCurrencies() {
        return new ResponseEntity<>(currenciesService.getAllCurrencies(), HttpStatus.OK);
    }

    @Operation(description = "get currency by code.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @GetMapping(value = "/{code}/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCurrencyByCode(@PathVariable @NotEmpty String code) {
        return new ResponseEntity<>(currenciesService.getCurrencyByCode(code), HttpStatus.OK);
    }

    @Operation(description = "delete currency by code.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @DeleteMapping(value = "/{code}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCurrency(@PathVariable @NotEmpty String code) {
        currenciesService.deleteCurrency(code);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(description = "Create new currency.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCurrency(@RequestBody @Valid CurrencyDTO currencyDTO) {
        currenciesService.createCurrency(currencyDTO);
        return new ResponseEntity(currencyDTO, HttpStatus.OK);
    }

    @Operation(description = "Update currency by code.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @PutMapping(value = "/{code}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCurrency(@PathVariable @NotEmpty String code, @RequestParam @NotEmpty String name, @RequestParam @NotEmpty double exRate) {
        CurrencyDTO currencyDTO = new CurrencyDTO(code, name, exRate);
        currenciesService.updateCurrency(currencyDTO);
        return new ResponseEntity(currencyDTO, HttpStatus.OK);
    }
}
