package io.maybank.currenciesservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Common error response.
 */
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Schema(name = ErrorResponseDTO.SCHEMA_NAME)
public class ErrorResponseDTO {

    /**
     * Name of schema to be referenced from eventual custom {@link io.swagger.v3.oas.annotations.responses.ApiResponse}.
     */
    public static final String SCHEMA_NAME = "ErrorResponse";

    /**
     * The error object.
     */
    private ErrorDTO error;

    @Override
    public String toString() {
        return String.valueOf(error);
    }
}