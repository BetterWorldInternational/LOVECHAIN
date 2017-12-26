package org.betterworldinternational.hugapi.validation;

import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserValidation {
    private final Validator validator;

    @Autowired
    public UserValidation(@Qualifier("requestValidator") Validator validator) {
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