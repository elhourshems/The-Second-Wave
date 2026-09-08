package com.thesecondwave.store.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ChangePasswordRequest", description = "Payload to change a user's password")
public class ChangePasswordRequest {
    @Schema(description = "User's current password", example = "OldPassw0rd")
    private String oldPassword;

    @Schema(description = "New password to set for the user", example = "NewPassw0rd")
    private String newPassword;
}
