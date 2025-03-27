package com.omnedu.timer_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TimerNotFoundException extends RuntimeException {
    public TimerNotFoundException(String message) {
        super(message);
    }
}