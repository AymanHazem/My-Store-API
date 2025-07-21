package com.ayman.my_store_api.carts;
import com.ayman.my_store_api.products.ProductNotFoundException;
import com.ayman.my_store_api.products.ProductRepository;
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
        if (cart==null) throw new CartNotFoundException();//resource not found
        var product = productRepository.findById(productId).orElse(null);
        if (product==null) throw new ProductNotFoundException();//user provide bad req
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

    public CartItemDto updateCartItem (UUID cartId , Long productId,Integer quantity)
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
