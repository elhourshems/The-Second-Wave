package com.thesecondwave.store.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UpdateUserRequest", description = "Payload to update an existing user's profile")
public class UpdateUserRequest {
    @Schema(description = "Updated full name of the user", example = "Kelly Slater")
    public String name;

    @Schema(description = "Updated email address of the user", example = "kelly.slater@example.com")
    public String email;
}
