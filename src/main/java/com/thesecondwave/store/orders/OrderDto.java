package com.thesecondwave.store.orders;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "Order", description = "A placed order, created from a checked-out cart")
public class OrderDto {
    @Schema(description = "Unique identifier of the order", example = "101")
    private Long id;

    @Schema(description = "Current status of the order", example = "PAID")
    private String status;

    @Schema(description = "Date and time the order was created", example = "2026-09-07T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Line items belonging to this order")
    private List<OrderItemDto> items;

    @Schema(description = "Total price of the order, summing all line items", example = "249.99")
    private BigDecimal totalPrice;
}
