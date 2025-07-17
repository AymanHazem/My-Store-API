package com.ayman.my_store_api.services;

import com.ayman.my_store_api.dtos.CheckOutRequest;
import com.ayman.my_store_api.dtos.CheckOutResponse;
import com.ayman.my_store_api.entities.Order;
import com.ayman.my_store_api.exceptions.CartEmptyException;
import com.ayman.my_store_api.exceptions.CartNotFoundException;
import com.ayman.my_store_api.exceptions.PaymentException;
import com.ayman.my_store_api.repositories.CartRepository;
import com.ayman.my_store_api.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor // only final
public class CheckOutService
{
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final PaymentGateway paymentGateway;
    @Transactional
    public CheckOutResponse checkout (CheckOutRequest request)
    {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
        if (cart.isEmpty())
            throw new CartEmptyException();
        var order = Order.fromCart(cart,authService.getCurrentUser());
        orderRepository.save(order);
        try
        {
            var session =  paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckOutResponse(order.getId() , session.getCheckoutUrl());
        }
        catch (PaymentException e)
        {
            orderRepository.delete(order);
            throw e;
        }
    }
}
