package org.betterworldinternational.hugapi.service;

import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.repository.UserRepository;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.TokenResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.AdditionalMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private static final String INVITATION_PIN = "invite";

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void registerUserWithWrongInvitationPin() {
        expectedException.expect(HugException.class);
        expectedException.expectMessage("Wrong invitation pin");

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setInvitationPin(INVITATION_PIN);

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getUserIdByInviteCode(registerRequest.getInvitationPin().toUpperCase())).thenReturn(null);

        new UserService(userRepository).registerUser(registerRequest);
    }

    @Test
    public void registerUserWithEmailAlreadyUsed() {
        expectedException.expect(HugException.class);
        expectedException.expectMessage("Email already used");

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("email");

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getUserIdByEmail(registerRequest.getEmail())).thenReturn(1);

        new UserService(userRepository).registerUser(registerRequest);
    }

    @Test
    public void registerUserWithoutChallenger() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getUserIdByInviteCode(anyString())).thenReturn(null);

        TokenResponse response = new UserService(userRepository).registerUser(new RegisterRequest());
        assertThat(response, is(notNullValue()));

        verify(userRepository).addUser(anyString(), anyString(), anyString(), anyString(), eq(null));
    }

    @Test
    public void registerUserWithChallenger() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setInvitationPin(INVITATION_PIN);

        UserRepository userRepository = mock(UserRepository.class);
        int challengerId = 1;
        String convertedInvitationPin = registerRequest.getInvitationPin().toUpperCase();
        when(userRepository.getUserIdByInviteCode(convertedInvitationPin)).thenReturn(challengerId);
        when(userRepository.getUserIdByInviteCode(AdditionalMatchers.not(eq(convertedInvitationPin)))).thenReturn(null);

        TokenResponse response = new UserService(userRepository).registerUser(registerRequest);
        assertThat(response, is(notNullValue()));

        verify(userRepository).addUser(anyString(), anyString(), anyString(), anyString(), eq(challengerId));
    }
}