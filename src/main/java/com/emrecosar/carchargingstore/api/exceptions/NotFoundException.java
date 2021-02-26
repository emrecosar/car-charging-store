package com.emrecosar.carchargingstore.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Resource not found exception
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    /**
     * Constructor
     */
    public NotFoundException() {
        super();
    }

}
