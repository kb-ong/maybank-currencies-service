package io.maybank.currenciesservice.service;

import io.maybank.currenciesservice.dto.Auth0UserProfile;

import java.util.Optional;

public interface GatewayService {
    Optional<Auth0UserProfile> getAuth0UserProfileByEmail(String email) throws Exception;
}
