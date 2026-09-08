package com.thesecondwave.store.carts;

import com.thesecondwave.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Cart")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Create cart", description = "Creates a new empty shopping cart and returns its identifier.", tags = {"Cart"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart created successfully."),
        @ApiResponse(responseCode = "400", description = "Bad request."),
        @ApiResponse(responseCode = "404", description = "Resource not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping
    public ResponseEntity<CartDto> createCart(
        UriComponentsBuilder uriBuilder
    ) {
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @Operation(summary = "Add item to cart", description = "Adds a product to an existing cart or increases its quantity if it is already present.", tags = {"Cart"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item added successfully."),
        @ApiResponse(responseCode = "400", description = "Bad request."),
        @ApiResponse(responseCode = "404", description = "Cart or product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
        @PathVariable UUID cartId,
        @RequestBody AddItemToCartRequest request) {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @Operation(summary = "Get cart", description = "Returns the contents and total price of a cart by its identifier.", tags = {"Cart"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully."),
        @ApiResponse(responseCode = "400", description = "Bad request."),
        @ApiResponse(responseCode = "404", description = "Cart not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        return cartService.getCart(cartId);
    }

    @Operation(summary = "Update cart item quantity", description = "Changes the quantity of a specific product inside a cart.", tags = {"Cart"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart item updated successfully."),
        @ApiResponse(responseCode = "400", description = "Bad request or invalid quantity."),
        @ApiResponse(responseCode = "404", description = "Cart or product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId,
        @Valid @RequestBody UpdateCartItemRequest request
    ) {
       return cartService.updateItem(cartId, productId, request.getQuantity());
    }

    @Operation(summary = "Remove item from cart", description = "Deletes a single product line item from the cart.", tags = {"Cart"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item removed successfully."),
        @ApiResponse(responseCode = "400", description = "Bad request."),
        @ApiResponse(responseCode = "404", description = "Cart or product not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId
    ) {
        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Clear cart", description = "Removes every item from the cart and resets the totals.", tags = {"Cart"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart cleared successfully."),
        @ApiResponse(responseCode = "400", description = "Bad request."),
        @ApiResponse(responseCode = "404", description = "Cart not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found."));
    }
}
