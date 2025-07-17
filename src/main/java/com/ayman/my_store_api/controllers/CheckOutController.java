package com.ayman.my_store_api.controllers;

import com.ayman.my_store_api.dtos.CheckOutRequest;
import com.ayman.my_store_api.dtos.ErrorDto;
import com.ayman.my_store_api.exceptions.CartEmptyException;
import com.ayman.my_store_api.exceptions.CartNotFoundException;
import com.ayman.my_store_api.services.CheckOutService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("checkout")
@AllArgsConstructor
public class CheckOutController
{
    private final CheckOutService checkOutService;

    @PostMapping
    public ResponseEntity<?> checkout (@Valid @RequestBody CheckOutRequest request)
    {
        try {
            return ResponseEntity.ok(checkOutService.checkout(request));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("Error Creating checkout session"));
        }
    }
    @ExceptionHandler({CartEmptyException.class, CartNotFoundException.class})
    public ResponseEntity<ErrorDto> handelException (Exception e)
    {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
