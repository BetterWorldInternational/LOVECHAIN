package org.betterworldinternational.hugapi.exception;

import org.betterworldinternational.hugapi.route.response.MessageResponse;

import java.util.List;

public class HugException extends RuntimeException {

    private List<MessageResponse.Field> errors;

    public HugException(String msg) {
        super(msg);
    }

    public List<MessageResponse.Field> getErrors() {
        return errors;
    }

    public void setErrors(List<MessageResponse.Field> errors) {
        this.errors = errors;
    }

}
