package org.betterworldinternational.hugapi;

import javax.validation.Validation;
import javax.validation.Validator;

class ValidatorFactory {
    public Validator create() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}