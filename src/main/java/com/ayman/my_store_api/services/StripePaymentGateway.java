package com.ayman.my_store_api.services;

import com.ayman.my_store_api.entities.Order;
import com.ayman.my_store_api.entities.OrderItem;
import com.ayman.my_store_api.entities.PaymentStatus;
import com.ayman.my_store_api.exceptions.PaymentException;
import com.ayman.my_store_api.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway
{
    private final OrderRepository orderRepository;
    @Value("${webSiteUrl}")
    private String webSiteUrl;
    @Value("${stripe.webhookSecretkey}")
    private String webhookSecretkey;

    @Override
    public CheckoutSession createCheckoutSession(Order order)
    {
        try
        {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCancelUrl(webSiteUrl+"/checkout-cancel")
                    .setSuccessUrl(webSiteUrl+"/checkout-success?orderId="+order.getId())
                    .putMetadata("order_id",order.getId().toString());

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

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request)
    {
        try
        {
            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload,signature,webhookSecretkey);
            return switch (event.getType())
            {
                case "payment_intent.succeeded" ->
                        Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));
                case "payment_intent.payment_failed" ->
                        Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));
                default -> Optional.empty();
            };
        }
        catch (SignatureVerificationException e)
        {
            throw new PaymentException("Invalid signature");
        }
    }

    private Long extractOrderId (Event event)
    {
        var stripeObject = event.getDataObjectDeserializer().getObject()
                .orElseThrow(()->new PaymentException("Could not deserialize Stripe event. Check the SDK and API version."));
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
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
