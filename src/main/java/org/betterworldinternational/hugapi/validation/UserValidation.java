package org.betterworldinternational.hugapi.validation;

import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.MessageResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserValidation {
    private final Validator validator;

    public UserValidation(Validator validator) {
        this.validator = validator;
    }

    public void validate(RegisterRequest registerRequest) {
        List<MessageResponse.Field> errors = new ArrayList<>();
        Set<ConstraintViolation<RegisterRequest>> constraintViolations = validator.validate(registerRequest);
        for (ConstraintViolation<RegisterRequest> constraintViolation : constraintViolations) {
            errors.add(new MessageResponse.Field(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
        }

        if (errors.size() != 0) {
            HugException exception = new HugException("validation error");
            exception.setErrors(errors);
            throw exception;
        }
    }
}