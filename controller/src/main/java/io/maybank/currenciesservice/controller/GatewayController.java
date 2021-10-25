package io.maybank.currenciesservice.controller;

import io.maybank.currenciesservice.dto.Auth0UserProfile;
import io.maybank.currenciesservice.dto.ErrorResponseDTO;
import io.maybank.currenciesservice.service.GatewayService;
import io.maybank.currenciesservice.service.exp.Auth0Exception;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/maybank-auth0-service/gateway")
@Tag(name = "Maybank: Gateway Service", description = "Maybank: Gateway Service Controller")
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    @Operation(description = "call Auth0 to get user info.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @GetMapping(value = "/Auth0", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAuth0User(@RequestParam @NotEmpty String email) {
        try {
            Auth0UserProfile auth0UserProfile =
                    gatewayService.getAuth0UserProfileByEmail(email).
                            orElseThrow(() -> new Auth0Exception("user profile not file by email:" + email, null));
            return new ResponseEntity<>(auth0UserProfile, HttpStatus.OK);
        } catch (Auth0Exception exp) {
            return new ResponseEntity<>(exp, HttpStatus.BAD_REQUEST);
        } catch (Exception exp) {
            return new ResponseEntity<>(exp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
