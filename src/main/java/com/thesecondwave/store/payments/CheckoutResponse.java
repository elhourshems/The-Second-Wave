package com.thesecondwave.store.payments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CheckoutResponse", description = "Response returned after starting a Stripe checkout session")
public class CheckoutResponse {
    @Schema(description = "Identifier of the order created for this checkout", example = "101")
    private Long orderId;

    @Schema(description = "URL of the Stripe-hosted checkout page to redirect the user to", example = "https://checkout.stripe.com/c/pay/cs_test_abc123")
    private String checkoutUrl;

    public CheckoutResponse(Long orderId, String checkoutUrl) {
        this.orderId = orderId;
        this.checkoutUrl = checkoutUrl;
    }
}
