package com.thesecondwave.store.carts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "CartProduct", description = "Snapshot of a product as it appears in a cart item")
public class ProductDto {
    @Schema(description = "Unique identifier of the product", example = "15")
    private Long id;

    @Schema(description = "Name of the product", example = "Channel Islands Fish 6'0\"")
    private String name;

    @Schema(description = "Price of the product", example = "399.99")
    private BigDecimal price;
}
