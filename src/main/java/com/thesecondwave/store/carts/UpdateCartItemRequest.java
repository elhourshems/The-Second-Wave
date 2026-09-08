package com.thesecondwave.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "UpdateCartItemRequest", description = "Payload to update the quantity of a cart item")
public class UpdateCartItemRequest {
    @Schema(description = "New quantity for the cart item, between 1 and 1000", example = "3")
    @NotNull(message = "Quantity must be provided.")
    @Min(value = 1, message = "Quantity must be greater than zero.")
    @Max(value = 1000, message = "Quantity must be less than or equal to 100.")
    private Integer quantity;
}
