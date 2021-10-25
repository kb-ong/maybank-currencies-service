package io.maybank.currenciesservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.maybank.currenciesservice.dto.Auth0UserProfile;
import io.maybank.currenciesservice.model.Auth0Token;
import io.maybank.currenciesservice.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.internal.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GatewayServiceImpl implements GatewayService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final static String TOKEN_SEGMENT = "/oauth/token";
    private final static String EMAIL_SEGMENT = "/api/v2/users-by-email";

    private final static String POST_METHOD = "POST";
    private final static String GET_METHOD = "GET";
    private final static String QUERYSTR_EMAIL = "email";
    private final static String HEADER_CONTENT_TYPE = "Content-Type";
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String CONTENT_TYPE = "application/json";
    private final static String BODY_CLIENT_ID = "client_id";
    private final static String BODY_CLIENT_SECRET = "client_secret";
    private final static String BODY_AUDIENCE = "audience";
    private final static String BODY_GRANTTYPE = "grant_type";

    private final String clientId;
    private final String clientSecret;
    private final String audience;
    private final String grantType;
    private final WebClient webClient;
    private final int maxRetries;
    private final int firstBackOff;

    private final Mono<Auth0Token> cachedToken = getCachedToken();

    private Mono<Auth0Token> getCachedToken() {
        return Mono.<Auth0Token>
                create(
                sink -> sink.success(getToken().block()))
                .onErrorContinue((e, o) -> log.warn("Get Token Failed", e))
                .cache(
                        token -> Duration.ofSeconds((long) (token.getExpiresIn() * 0.9)),
                        error -> Duration.ZERO,
                        () -> Duration.ZERO
                );

    }

    public GatewayServiceImpl(
            @Value("${auth0.max-retries}") int maxRetries,
            @Value("${auth0.first-back-off}") int firstBackOff,
            @Value("${auth0.base_url}") String baseURL,
            @Value("${auth0.client_id}") String clientID,
            @Value("${auth0.client_secret}") String clientSecret,
            @Value("${auth0.audience}") String audience,
            @Value("${auth0.grant_type}") String grantType) {
        this.webClient = WebClient.builder().baseUrl(baseURL).build();
        this.clientId = clientID;
        this.clientSecret = clientSecret;
        this.maxRetries = maxRetries;
        this.firstBackOff = firstBackOff;
        this.audience = audience;
        this.grantType = grantType;
    }

    private Mono<Auth0Token> getToken() {
        MultiValueMap<String, String> formDataMap = new LinkedMultiValueMap<>();
        formDataMap.add(BODY_CLIENT_ID, clientId);
        formDataMap.add(BODY_CLIENT_SECRET, clientSecret);
        formDataMap.add(BODY_AUDIENCE, audience);
        formDataMap.add(BODY_GRANTTYPE, grantType);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(TOKEN_SEGMENT).build())
                .body(BodyInserters.fromFormData(formDataMap))
                .retrieve()
                .bodyToMono(Auth0Token.class)
                .retryBackoff(maxRetries, Duration.ofMillis(firstBackOff));
    }

    private List<Auth0UserProfile> getUserInfoByEmail(String email, String accessToken) throws JsonProcessingException {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(EMAIL_SEGMENT)
                                .queryParam(QUERYSTR_EMAIL, email)
                                .build()
                )
                .header("Authorization", accessToken)
                .retrieve()
                .bodyToFlux(Auth0UserProfile.class)
                .collect(Collectors.toList())
                .retryBackoff(maxRetries, Duration.ofMillis(firstBackOff))
                .block();
    }

    @Override
    public Optional<Auth0UserProfile> getAuth0UserProfileByEmail(String email) throws Exception {
        String accessToken = "bearer " + cachedToken.block().getAccessToken();
        if (!Strings.isEmpty(accessToken)) {
            List<Auth0UserProfile> result = getUserInfoByEmail(email, accessToken);
            if (result.size() > 0)
                return Optional.of(result.get(0));
        } else {
            log.error("Token is empty.");
        }
        return Optional.empty();
    }
}
