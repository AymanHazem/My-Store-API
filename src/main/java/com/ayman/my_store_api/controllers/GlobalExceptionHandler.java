package com.ayman.my_store_api.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handelValidationErrors (MethodArgumentNotValidException exception)
    {
        var errors = new HashMap<String,String>();

        exception.getBindingResult().getFieldErrors().forEach(error->{errors.put(error.getField(),error.getDefaultMessage());});

        return ResponseEntity.badRequest().body(errors);
    }
}
