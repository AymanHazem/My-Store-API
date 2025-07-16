package com.ayman.my_store_api.controllers;


import com.ayman.my_store_api.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handelMessageNotReadable ()
    {
        return ResponseEntity.badRequest().body(new ErrorDto("Invalid Request Body."));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handelValidationErrors (MethodArgumentNotValidException exception)
    {
        var errors = new HashMap<String,String>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error->
                {errors.put(error.getField(),error.getDefaultMessage());});

        return ResponseEntity.badRequest().body(errors);
    }
}
