package com.thesecondwave.store.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(name = "User", description = "A registered user account")
public class UserDto {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Full name of the user", example = "Kelly Slater")
    private String name;

    @Schema(description = "Email address of the user", example = "kelly.slater@example.com")
    private String email;
}
