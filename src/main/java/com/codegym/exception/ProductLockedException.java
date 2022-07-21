package com.codegym.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ProductLockedException extends RuntimeException{
    public ProductLockedException(String message) {
        super(message);
    }
}
