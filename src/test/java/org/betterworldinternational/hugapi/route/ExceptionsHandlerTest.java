package org.betterworldinternational.hugapi.route;

import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.route.response.MessageResponse;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.contains;

public class ExceptionsHandlerTest {
    @Test
    public void hugException() {
        HugException exception = new HugException("Test exception");
        MessageResponse.Field field = new MessageResponse.Field("Test key", "test message");
        exception.setErrors(Collections.singletonList(field));
        ResponseEntity<MessageResponse> responseEntity = new ExceptionsHandler().handleHugException(exception);
        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.BAD_REQUEST)));
        assertThat(responseEntity.getBody().isSuccess(), is(false));
        assertThat(responseEntity.getBody().getMessage(), is(equalTo(exception.getMessage())));

        assertThat(responseEntity.getBody().getErrors(), hasSize(1));
        assertThat(responseEntity.getBody().getErrors(), contains(field));

        assertThat(responseEntity.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON));
        assertThat(responseEntity.getHeaders().getFirst(HeaderConstants.SERVER_HEADER_NAME),
                is(equalTo(HeaderConstants.SERVER_HEADER_VALUE)));
    }

    @Test
    public void otherExceptions() {
        ResponseEntity<MessageResponse> responseEntity =
                new ExceptionsHandler().handleExceptions(new RuntimeException());
        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.INTERNAL_SERVER_ERROR)));
        assertThat(responseEntity.getBody().isSuccess(), is(false));
        assertThat(responseEntity.getBody().getMessage(), is(equalTo("Something went wrong")));

        assertThat(responseEntity.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON));
        assertThat(responseEntity.getHeaders().getFirst(HeaderConstants.SERVER_HEADER_NAME),
                is(equalTo(HeaderConstants.SERVER_HEADER_VALUE)));
    }
}