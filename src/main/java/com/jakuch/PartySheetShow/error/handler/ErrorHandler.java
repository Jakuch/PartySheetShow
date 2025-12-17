package com.jakuch.PartySheetShow.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        ModelAndView modelAndView = new ModelAndView("imNotWorkingBro");
        modelAndView.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        return modelAndView;
    }
}
