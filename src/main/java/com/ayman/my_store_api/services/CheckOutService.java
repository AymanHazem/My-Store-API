package com.ayman.my_store_api.services;

import com.ayman.my_store_api.dtos.CheckOutRequest;
import com.ayman.my_store_api.dtos.CheckOutResponse;
import com.ayman.my_store_api.entities.Order;
import com.ayman.my_store_api.exceptions.CartEmptyException;
import com.ayman.my_store_api.exceptions.CartNotFoundException;
import com.ayman.my_store_api.repositories.CartRepository;
import com.ayman.my_store_api.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor // only final
public class CheckOutService
{
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AuthService authService;
    @Value("${webSiteUrl}")
    private String webSiteUrl;
    @Transactional
    public CheckOutResponse checkout (CheckOutRequest request) throws StripeException {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
        if (cart.isEmpty())
            throw new CartEmptyException();
        var order = Order.fromCart(cart,authService.getCurrentUser());
        orderRepository.save(order);

        // create cart session
        try
        {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCancelUrl(webSiteUrl+"/checkout-cancel")
                    .setSuccessUrl(webSiteUrl+"/checkout-success?orderId="+order.getId());

            order.getItems()
                  .forEach(item ->
                  {
                      var lineItem = SessionCreateParams.LineItem.builder().setQuantity(Long.valueOf(item.getQuantity()))
                              .setPriceData(
                                      SessionCreateParams.LineItem.PriceData.builder()
                                              .setCurrency("usd")
                                              .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                                              .setProductData(
                                                      SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                              .setName(item.getProduct().getName())
                                                              .build()
                                              ).build()
                              ).build();
                      builder.addLineItem(lineItem);
                  });

            var session =  Session.create(builder.build()) ;
            cartService.clearCart(cart.getId());
            return new CheckOutResponse(order.getId() , session.getUrl());
        } catch (StripeException e)
        {
            orderRepository.delete(order);
            throw e;
        }
    }
}
