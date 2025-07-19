package com.ayman.my_store_api.payments;

import com.ayman.my_store_api.common.ErrorDto;
import com.ayman.my_store_api.carts.CartEmptyException;
import com.ayman.my_store_api.carts.CartNotFoundException;
import com.ayman.my_store_api.orders.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("checkout")
@RequiredArgsConstructor
public class CheckOutController
{
    private final CheckOutService checkOutService;
    private final OrderRepository orderRepository;


    @PostMapping
    public CheckOutResponse checkout (@Valid @RequestBody CheckOutRequest request)
    {
        return checkOutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handelWebhook (@RequestHeader Map<String,String> headers , @RequestBody String payload)
    {
        checkOutService.handelWebhookEvent(new WebhookRequest(headers,payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handelPaymentException ()
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("Error Creating checkout session"));
    }

    @ExceptionHandler({CartEmptyException.class, CartNotFoundException.class})
    public ResponseEntity<ErrorDto> handelCartExceptions (Exception e)
    {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
