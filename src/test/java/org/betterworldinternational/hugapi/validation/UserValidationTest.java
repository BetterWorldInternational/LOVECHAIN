package org.betterworldinternational.hugapi.validation;

import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.MessageResponse;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserValidationTest {
    @Test
    public void withoutViolations() {
        RegisterRequest registerRequest = new RegisterRequest();
        new UserValidation(createValidator(registerRequest, Collections.emptySet())).validate(registerRequest);
    }

    @Test
    public void withViolations() {
        try {
            Set<ConstraintViolation<RegisterRequest>> violations = new HashSet<>();
            violations.add(createViolation("path1", "message1"));
            violations.add(createViolation("path2", "message2"));

            RegisterRequest registerRequest = new RegisterRequest();
            new UserValidation(createValidator(registerRequest, violations)).validate(registerRequest);
            fail(HugException.class.getSimpleName() + " should be generated");
        } catch (HugException e) {
            assertThat(e.getErrors(), hasSize(2));
            assertThat(e.getErrors(), hasItems(
                    new MessageResponse.Field("path1", "message1"),
                    new MessageResponse.Field("path2", "message2")));
        }
    }

    private static ConstraintViolation<RegisterRequest> createViolation(String propertyPath, String message) {
        Path path = mock(Path.class);
        when(path.toString()).thenReturn(propertyPath);

        @SuppressWarnings("unchecked")
        ConstraintViolation<RegisterRequest> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn(message);
        return violation;
    }

    private static Validator createValidator(
            RegisterRequest registerRequest, Set<ConstraintViolation<RegisterRequest>> violations) {

        Validator validator = mock(Validator.class);
        when(validator.validate(registerRequest)).thenReturn(violations);
        return validator;
    }
}