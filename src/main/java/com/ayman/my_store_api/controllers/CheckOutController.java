package com.ayman.my_store_api.controllers;

import com.ayman.my_store_api.dtos.CheckOutRequest;
import com.ayman.my_store_api.dtos.CheckOutResponse;
import com.ayman.my_store_api.entities.Order;
import com.ayman.my_store_api.entities.OrderItem;
import com.ayman.my_store_api.entities.OrderStatus;
import com.ayman.my_store_api.repositories.CartRepository;
import com.ayman.my_store_api.repositories.OrderRepository;
import com.ayman.my_store_api.services.AuthService;
import com.ayman.my_store_api.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("checkout")
@AllArgsConstructor
public class CheckOutController
{
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> checkout (@Valid @RequestBody CheckOutRequest request)
    {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null)
            return ResponseEntity.badRequest().body(Map.of("error","Cart not Found."));
        if (cart.getItems().isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error","Cart is Empty."));

        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getItems()
                .forEach(
                item ->
                {
                    var orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setTotalPrice(item.getTotalPrice());
                    orderItem.setProduct(item.getProduct());
                    orderItem.setUnitPrice(item.getProduct().getPrice());
                    order.getItems().add(orderItem);
                });
        orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return ResponseEntity.ok(new CheckOutResponse(order.getId()));
    }
}
