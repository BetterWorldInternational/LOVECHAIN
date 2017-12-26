package org.betterworldinternational.hugapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
class ValidatorFactory {
    @Bean("requestValidator")
    public Validator create() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}