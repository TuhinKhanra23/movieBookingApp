package com.cts.fse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MovieBookingException.class)
    public Map<String,String> handleExpections(MovieBookingException ex){
        Map<String,String>errorMap=new HashMap<>();
        errorMap.put("errorMsg", ex.getMessage());
        return errorMap;
    }
}
