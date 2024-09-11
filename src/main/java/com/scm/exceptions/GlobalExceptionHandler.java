package com.scm.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String resourceNotFound(ResourceNotFoundException ex, Model model) {
        log.info("{}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return  "error-404";
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(UnauthorizedException ex, Model model) {
        log.info("{}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "error";
    }


    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalServerError(HttpServerErrorException.InternalServerError ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return  "error";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String runtimeException(RuntimeException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return  "error";
    }

}
