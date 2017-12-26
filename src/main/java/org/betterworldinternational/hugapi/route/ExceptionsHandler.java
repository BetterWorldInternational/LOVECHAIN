package org.betterworldinternational.hugapi.route;

import org.betterworldinternational.hugapi.route.response.MessageResponse;
import org.betterworldinternational.hugapi.exception.HugException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(value = { HugException.class })
    public ResponseEntity<Object> handleHugException(HugException e) {
        logger.info("Handling external error: {}", e.getMessage());

        MessageResponse messageResponse = new MessageResponse(false, e.getMessage());
        messageResponse.setErrors(e.getErrors());

        return new ResponseEntity<>(messageResponse, createHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<Object> handleExceptions(RuntimeException e) {
        logger.warn("Handling internal error:", e);

        return new ResponseEntity<>(
                new MessageResponse(false, "Something went wrong"),
                createHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HeaderConstants.SERVER_HEADER_NAME, HeaderConstants.SERVER_HEADER_VALUE);
        return headers;
    }
}