package com.thesecondwave.store.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(name = "LoginResponse", description = "Response returned after a successful login, containing access and refresh tokens")
public class LoginResponse {
    @Schema(description = "Short-lived access token (expires in 15 minutes)", type = "string", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.abc123")
    private Jwt accessToken;

    @Schema(description = "Long-lived refresh token (expires in 7 days)", type = "string", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.def456")
    private Jwt refreshToken;
}
