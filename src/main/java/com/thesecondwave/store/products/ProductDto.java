package com.thesecondwave.store.products;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "Product", description = "A second-hand surf equipment product listed for sale")
public class ProductDto {
    @Schema(description = "Unique identifier of the product", example = "15")
    private Long id;

    @Schema(description = "Name of the product", example = "Channel Islands Fish 6'0\"")
    private String name;

    @Schema(description = "Price of the product", example = "399.99")
    private BigDecimal price;

    @Schema(description = "Detailed description of the product's condition and features", example = "Lightly used, one small ding repaired on the rail")
    private String description;

    @Schema(description = "Identifier of the category this product belongs to", example = "2")
    private Byte categoryId;
}
