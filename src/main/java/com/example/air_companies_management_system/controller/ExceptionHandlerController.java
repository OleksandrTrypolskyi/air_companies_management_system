package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = AirCompanyNotFoundException.class)
    public @ResponseBody
    ErrorInfo airCompanyNotFoundException(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NumberFormatException.class)
    public @ResponseBody
    ErrorInfo numberFormatException(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }
}
