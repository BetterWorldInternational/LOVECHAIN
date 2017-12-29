package org.betterworldinternational.hugapi.service;

import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.repository.UserRepository;
import org.betterworldinternational.hugapi.route.request.DidItRequest;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.EffectResponse;
import org.betterworldinternational.hugapi.route.response.MapPin;
import org.betterworldinternational.hugapi.route.response.TokenResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.AdditionalMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
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

    @Test
    public void userDidItWithImage() {
        checkUserDidIt(true);
    }

    @Test
    public void userDidItWithoutImage() {
        checkUserDidIt(false);
    }

    @Test
    public void getMap() {
        UserRepository userRepository = mock(UserRepository.class);
        List<MapPin> expectedResult = new ArrayList<>();
        when(userRepository.getMap()).thenReturn(expectedResult);

        List<MapPin> actualResult = new UserService(userRepository).getMap();
        assertSame(expectedResult, actualResult);
    }

    @Test
    public void getEffect() {
        UserRepository userRepository = mock(UserRepository.class);
        int userId = 1;
        int effectCount = 2;
        when(userRepository.getEffectCount(userId)).thenReturn(effectCount);

        int activatesCount = 3;
        when(userRepository.getActivatesCount(userId)).thenReturn(activatesCount);

        EffectResponse response = new UserService(userRepository).getEffect(userId);
        assertThat(response.getTotal(), is(effectCount));
        assertThat(response.getActivates(), is(activatesCount));
    }

    @Test
    public void getUserIdByTokenWithZeroId() {
        expectedException.expect(HugException.class);
        expectedException.expectMessage("Unauthorized");

        UserRepository userRepository = mock(UserRepository.class);
        String token = "token";
        when(userRepository.getUserIdByToken(token)).thenReturn(0);

        new UserService(userRepository).getUserIdByToken(token);
    }

    @Test
    public void getUserIdByTokenWithNonZeroId() {
        UserRepository userRepository = mock(UserRepository.class);
        String token = "token";
        int expectedUserId = 1;
        when(userRepository.getUserIdByToken(token)).thenReturn(expectedUserId);

        int actualUserId = new UserService(userRepository).getUserIdByToken(token);
        assertEquals(expectedUserId, actualUserId);
    }

    @Test
    public void getAllActivates() {
        UserRepository userRepository = mock(UserRepository.class);
        int expectedAllActivatesCount = 7;
        when(userRepository.getAllActivatesCount()).thenReturn(expectedAllActivatesCount);

        int actualAllActivatesCount = new UserService(userRepository).getAllActivates();
        assertEquals(expectedAllActivatesCount, actualAllActivatesCount);
    }

    @Test
    public void getAllEffect() {
        UserRepository userRepository = mock(UserRepository.class);
        int expectedAllEffectCount = 4;
        when(userRepository.getAllEffectCount()).thenReturn(expectedAllEffectCount);

        int actualAllEffectCount = new UserService(userRepository).getAllEffect();
        assertEquals(expectedAllEffectCount, actualAllEffectCount);
    }

    @Test
    public void getEffectByInviteCodeWithoutUserId() {
        assertEquals(0, new UserService(null).getEffectByInviteCode(null));
    }

    @Test
    public void getEffectByInviteCodeWithUserId() {
        UserRepository userRepository = mock(UserRepository.class);
        int userId = 1;
        when(userRepository.getUserIdByInviteCode(INVITATION_PIN)).thenReturn(userId);

        int expectedEffectCount = 2;
        when(userRepository.getEffectCount(userId)).thenReturn(expectedEffectCount);

        int actualEffectCount = new UserService(userRepository).getEffectByInviteCode(INVITATION_PIN);
        assertEquals(expectedEffectCount, actualEffectCount);
    }

    @Test
    public void getActivatesByInviteCodeWithoutUserId() {
        assertEquals(0, new UserService(null).getActivatesByInviteCode(null));
    }

    @Test
    public void getActivatesByInviteCodeWithUserId() {
        UserRepository userRepository = mock(UserRepository.class);
        int userId = 2;
        when(userRepository.getUserIdByInviteCode(INVITATION_PIN)).thenReturn(userId);

        int expectedActivatesCount = 7;
        when(userRepository.getActivatesCount(userId)).thenReturn(expectedActivatesCount);

        int actualActivatesCount = new UserService(userRepository).getActivatesByInviteCode(INVITATION_PIN);
        assertEquals(expectedActivatesCount, actualActivatesCount);
    }

    private static void checkUserDidIt(boolean hasImage) {
        UserRepository userRepository = mock(UserRepository.class);
        String initialImage = hasImage ? "image" : null;

        DidItRequest request = new DidItRequest();
        request.setLatitude(1.0);
        request.setLongitude(2.0);

        int userId = 7;
        new UserService(userRepository).userDidIt(userId, initialImage, request);

        String finalImage = hasImage ? initialImage : "default";
        verify(userRepository).userDidIt(userId, finalImage, request.getLatitude(), request.getLongitude());
    }
}