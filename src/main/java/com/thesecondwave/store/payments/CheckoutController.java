package com.thesecondwave.store.payments;

import com.thesecondwave.store.common.ErrorDto;
import com.thesecondwave.store.carts.CartEmptyException;
import com.thesecondwave.store.carts.CartNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
@Tag(name = "Payment")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @Operation(summary = "Create checkout session", description = "Starts a Stripe checkout flow for the specified cart and returns the checkout URL.", tags = {"Payment"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Checkout session created successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid cart or checkout request."),
        @ApiResponse(responseCode = "404", description = "Cart not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error while creating checkout.")
    })
    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @Operation(summary = "Stripe webhook handler", description = "Processes Stripe callback events such as successful checkout and payment status changes.", tags = {"Payment"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Webhook processed successfully."),
        @ApiResponse(responseCode = "400", description = "Bad webhook payload."),
        @ApiResponse(responseCode = "404", description = "Resource not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error while processing the webhook.")
    })
    @PostMapping("/webhook")
    public void handleWebhook(
        @RequestHeader Map<String, String> headers,
        @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }


    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
