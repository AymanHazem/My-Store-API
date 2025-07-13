package com.ayman.my_store_api.controllers;
import com.ayman.my_store_api.dtos.AddItemToCartRequest;
import com.ayman.my_store_api.dtos.CartDto;
import com.ayman.my_store_api.dtos.CartItemDto;
import com.ayman.my_store_api.dtos.UpdateCartItemRequest;
import com.ayman.my_store_api.entities.Cart;
import com.ayman.my_store_api.mappers.CartMapper;
import com.ayman.my_store_api.repositories.CartRepository;
import com.ayman.my_store_api.repositories.ProductRepository;
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
public class CartController
{
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart (UriComponentsBuilder uriBuilder)
    {
        var cart = new Cart();
        cartRepository.save(cart);
        var cartDto =cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId  , @RequestBody AddItemToCartRequest request)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) return ResponseEntity.notFound().build();//resours not found
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product==null) return ResponseEntity.badRequest().build();//user provid bad req
        var cartItem =  cart.addItem(product);
        cartRepository.save(cart);
        var cartItemDto= cartMapper.toDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart (@PathVariable UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable UUID cartId , @PathVariable long productId , @RequestBody UpdateCartItemRequest request)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error" , "Cart Not Found"));
        var cartItem = cart.getItem(productId);
        if (cartItem==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error" , "Product Not Found"));
        cartItem.setQuantity(request.getQuantity()+1);
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable UUID cartId , @PathVariable long productId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error" , "Cart Not Found"));
        var cartItem = cart.getItem(productId);
        if (cartItem==null) return ResponseEntity.notFound().build();
        cart.removeItem(productId);
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        cart.clear();
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }

}
