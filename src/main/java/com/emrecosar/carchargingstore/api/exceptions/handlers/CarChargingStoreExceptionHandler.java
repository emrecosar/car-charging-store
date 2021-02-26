package com.emrecosar.carchargingstore.api.exceptions.handlers;

import com.emrecosar.carchargingstore.api.exceptions.BadRequestException;
import com.emrecosar.carchargingstore.api.exceptions.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring's controller advice usage to override exceptions and customize body
 */
@ControllerAdvice
public class CarChargingStoreExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Override handleMethodArgumentNotValid to validate and return customized messages for request object validations
     *
     * @param ex      the exception
     * @param headers the header
     * @param status  the status code
     * @param request the request
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                          HttpStatus status, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return buildResponseEntity(errorMessage, status);
    }

    /**
     * Handle customize BadRequestException
     *
     * @param ex the bad request exception
     * @return a response entity wrapping the raised exception
     */
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity handleBadRequest(BadRequestException ex) {
        return buildResponseEntity(ex.getMessage(), BadRequestException.HTTP_STATUS);
    }

    /**
     * Handle customize NotFoundException
     *
     * @param ex the exception
     * @return a response entity wrapping the raised exception
     */
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity handleNotFound(NotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), NotFoundException.HTTP_STATUS);
    }

    /**
     * Build response body as json
     *
     * @param message    the response message
     * @param httpStatus the http status
     * @return ResponseEntity
     */
    private ResponseEntity buildResponseEntity(Object message, HttpStatus httpStatus) {
        ResponseEntity responseEntity;
        if (message == null) {
            responseEntity = new ResponseEntity(null, null, httpStatus);
        } else {
            Map<String, Object> body = new HashMap<>();
            body.put("message", message);
            responseEntity = new ResponseEntity(body, null, httpStatus);
        }
        return responseEntity;
    }

}
