package com.thesecondwave.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AddItemToCartRequest", description = "Payload to add a product to a cart")
public class AddItemToCartRequest {
    @Schema(description = "Identifier of the product to add to the cart", example = "15")
    private Long productId;
}
