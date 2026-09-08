package com.thesecondwave.store.orders;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "OrderItem", description = "A single product line item within an order")
public class OrderItemDto {
    @Schema(description = "Product purchased in this line item")
    private ProductDto product;

    @Schema(description = "Quantity of the product purchased", example = "2")
    private int quantity;

    @Schema(description = "Total price for this line item (unit price times quantity)", example = "199.98")
    private BigDecimal totalPrice;
}
