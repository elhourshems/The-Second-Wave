package com.thesecondwave.store.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "RegisterUserRequest", description = "Payload to register a new user account")
public class RegisterUserRequest {
    @Schema(description = "Full name of the user", example = "Kelly Slater")
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Email address of the user, must be lowercase", example = "kelly.slater@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Lowercase(message = "Email must be in lowercase")
    private String email;

    @Schema(description = "Password for the account, between 6 and 25 characters", example = "SurfsUp123")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters long.")
    private String password;
}
