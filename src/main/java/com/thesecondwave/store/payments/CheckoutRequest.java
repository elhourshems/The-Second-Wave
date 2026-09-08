package com.thesecondwave.store.payments;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "CheckoutRequest", description = "Payload to start a checkout session for a cart")
public class CheckoutRequest {
    @Schema(description = "Identifier of the cart to check out", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "Cart ID is required.")
    private UUID cartId;
}
