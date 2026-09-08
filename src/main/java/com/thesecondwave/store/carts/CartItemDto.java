package com.thesecondwave.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "CartItem", description = "A single product line item within a cart")
public class CartItemDto {
    @Schema(description = "Product added to the cart")
    private ProductDto product;

    @Schema(description = "Quantity of the product in the cart", example = "1")
    private int quantity;

    @Schema(description = "Total price for this line item (unit price times quantity)", example = "399.99")
    private BigDecimal totalPrice;
}
