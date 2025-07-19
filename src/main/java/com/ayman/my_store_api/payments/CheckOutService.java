package com.ayman.my_store_api.payments;

import com.ayman.my_store_api.orders.Order;
import com.ayman.my_store_api.carts.CartEmptyException;
import com.ayman.my_store_api.carts.CartNotFoundException;
import com.ayman.my_store_api.carts.CartRepository;
import com.ayman.my_store_api.orders.OrderRepository;
import com.ayman.my_store_api.auth.AuthService;
import com.ayman.my_store_api.carts.CartService;
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

    public void handelWebhookEvent (WebhookRequest request)
    {
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult ->
        {
            var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
        });

    }
}
