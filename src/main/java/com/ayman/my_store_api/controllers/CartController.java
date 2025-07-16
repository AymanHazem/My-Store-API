package com.ayman.my_store_api.controllers;
import com.ayman.my_store_api.dtos.*;
import com.ayman.my_store_api.exceptions.CartNotFoundException;
import com.ayman.my_store_api.exceptions.ProductNotFoundException;
import com.ayman.my_store_api.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping ("carts")
@AllArgsConstructor
@Tag(name = "Carts")
public class CartController
{
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart (UriComponentsBuilder uriBuilder)
    {
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Adds Product to the Cart")
    public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId  , @RequestBody AddItemToCartRequest request)
    {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart (@PathVariable UUID cartId)
    {
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCartItem(@PathVariable UUID cartId , @PathVariable long productId , @RequestBody UpdateCartItemRequest request)
    {
        return cartService.updateCartItem(cartId,productId, request.getQuantity());
    }

    @DeleteMapping("{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable UUID cartId , @PathVariable long productId)
    {
        cartService.removeItem(cartId,productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId)
    {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto>handelCartNotFound ()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("Cart Not Found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto>handelProductNotFound ()
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Product Not Found in the Cart"));
    }
}
