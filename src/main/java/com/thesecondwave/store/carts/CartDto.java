package com.thesecondwave.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Schema(name = "Cart", description = "A shopping cart holding products a user intends to purchase")
public class CartDto {
    @Schema(description = "Unique identifier of the cart", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Line items currently in the cart")
    private List<CartItemDto> items = new ArrayList<>();

    @Schema(description = "Total price of all items in the cart", example = "399.99")
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
