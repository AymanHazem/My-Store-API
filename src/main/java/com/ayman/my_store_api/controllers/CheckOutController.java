package com.ayman.my_store_api.controllers;

import com.ayman.my_store_api.dtos.CheckOutRequest;
import com.ayman.my_store_api.dtos.CheckOutResponse;
import com.ayman.my_store_api.dtos.ErrorDto;
import com.ayman.my_store_api.exceptions.CartEmptyException;
import com.ayman.my_store_api.exceptions.CartNotFoundException;
import com.ayman.my_store_api.services.CheckOutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("checkout")
@AllArgsConstructor
public class CheckOutController
{
    private final CheckOutService checkOutService;

    @PostMapping
    public CheckOutResponse checkout (@Valid @RequestBody CheckOutRequest request)
    {
        return checkOutService.checkout(request);
    }
    @ExceptionHandler({CartEmptyException.class, CartNotFoundException.class})
    public ResponseEntity<ErrorDto> handelException (Exception e)
    {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
