package com.ayman.my_store_api.services;

import com.ayman.my_store_api.entities.Order;
import com.ayman.my_store_api.entities.OrderItem;
import com.ayman.my_store_api.exceptions.PaymentException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class StripePaymentGateway implements PaymentGateway
{
    @Value("${webSiteUrl}")
    private String webSiteUrl;
    @Override
    public CheckoutSession createCheckoutSession(Order order)
    {
        try
        {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCancelUrl(webSiteUrl+"/checkout-cancel")
                    .setSuccessUrl(webSiteUrl+"/checkout-success?orderId="+order.getId());

            order.getItems().forEach(item -> {var lineItem = createLineItem(item) ; builder.addLineItem(lineItem);});

            var session =  Session.create(builder.build()) ;
            return new CheckoutSession(session.getUrl());
        }
        catch (StripeException e)
        {
            System.out.println(e.getMessage());
            throw new PaymentException();
        }
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item)
    {
        return SessionCreateParams.LineItem.builder().setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item))
                .build();

    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item)
    {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item)
    {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}
