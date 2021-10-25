package io.maybank.currenciesservice.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class Auth0UserProfile {
    private String email;
    private String name;
    private String nickname;
    private String picture;
    private String user_id;
    private Instant created_at;
    private Instant updated_at;
    private boolean email_verified;
}
