package com.thesecondwave.store.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "JwtResponse", description = "Wrapper response containing a single JWT token")
public class JwtResponse {
    @Schema(description = "Encoded JWT token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.abc123")
    private String token;
}
