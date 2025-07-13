package com.ayman.my_store_api.services;
import com.ayman.my_store_api.dtos.CartDto;
import com.ayman.my_store_api.dtos.CartItemDto;
import com.ayman.my_store_api.entities.Cart;
import com.ayman.my_store_api.exceptions.CartNotFoundException;
import com.ayman.my_store_api.exceptions.ProductNotFoundException;
import com.ayman.my_store_api.mappers.CartMapper;
import com.ayman.my_store_api.repositories.CartRepository;
import com.ayman.my_store_api.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService
{
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public CartDto createCart ()
    {
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart (UUID cartId , long productId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) throw new CartNotFoundException();//resours not found
        var product = productRepository.findById(productId).orElse(null);
        if (product==null) throw new ProductNotFoundException();//user provid bad req
        var cartItem =  cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart (UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) throw new CartNotFoundException();
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem (UUID cartId , long productId,Integer quantity)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) throw new CartNotFoundException();
        var cartItem = cart.getItem(productId);
        if (cartItem==null) throw new ProductNotFoundException();
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void removeItem (UUID cartId , long productId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) throw new CartNotFoundException();
        var cartItem = cart.getItem(productId);
        if (cartItem==null) throw new ProductNotFoundException();
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart (UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null) throw new CartNotFoundException();
        cart.clear();
        cartRepository.save(cart);
    }
}
