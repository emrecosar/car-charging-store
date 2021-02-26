package com.emrecosar.carchargingstore.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message);
    }

}
