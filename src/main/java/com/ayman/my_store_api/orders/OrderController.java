package com.ayman.my_store_api.orders;

import com.ayman.my_store_api.common.ErrorDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("orders")
@AllArgsConstructor
public class OrderController
{
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders ()
    {
        return orderService.getAllOrders();
    }
    @GetMapping("/{orderId}")
    public OrderDto getOrder (@PathVariable("orderId") long orderId)
    {
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> handleOrderNotFound ()
    {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied (Exception e)
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(e.getMessage()));
    }
}
