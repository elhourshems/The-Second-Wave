package com.thesecondwave.store.payments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Schema(name = "WebhookRequest", description = "Raw Stripe webhook event, carrying its headers and payload for signature verification")
public class WebhookRequest {
    @Schema(description = "HTTP headers received with the webhook request, including the Stripe signature")
    private Map<String, String> headers;

    @Schema(description = "Raw JSON payload of the webhook event", example = "{\"id\":\"evt_1\",\"type\":\"checkout.session.completed\"}")
    private String payload;
}
