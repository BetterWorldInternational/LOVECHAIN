package org.betterworldinternational.hugapi.route;

import org.betterworldinternational.hugapi.route.request.DidItRequest;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.EffectResponse;
import org.betterworldinternational.hugapi.route.response.MapPin;
import org.betterworldinternational.hugapi.route.response.MessageResponse;
import org.betterworldinternational.hugapi.route.response.TokenResponse;
import org.betterworldinternational.hugapi.service.UserService;
import org.betterworldinternational.hugapi.validation.UserValidation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private final static String IMAGE = "image";
    private final static String IMAGE_PATH = "https://s3.amazonaws.com/hug-challenge/";
    private final static String DEFAULT_IMAGE = "map_pin.png";
    private final static String TOKEN = "token";
    private final static int USER_ID = 7;

    @Test
    public void register() {
        RegisterRequest registerRequest = new RegisterRequest();
        UserService userService = mock(UserService.class);
        TokenResponse expectedResponse = new TokenResponse(null, null);
        when(userService.registerUser(registerRequest)).thenReturn(expectedResponse);

        UserValidation userValidation = mock(UserValidation.class);
        TokenResponse actualResponse = new UserController(userService, userValidation).register(registerRequest);
        assertSame(expectedResponse, actualResponse);

        verify(userValidation).validate(registerRequest);
    }

    @Test
    public void map() {
        List<MapPin> maps = new UserController(createUserServiceWithMap(), null).map();
        List<String> actualImages = maps.stream()
                .map(mapPin -> mapPin.getImage())
                .collect(Collectors.toList());
        assertThat(actualImages, is(equalTo(Arrays.asList(IMAGE_PATH + IMAGE, IMAGE_PATH + DEFAULT_IMAGE))));
    }

    private static UserService createUserServiceWithMap() {
        UserService userService = mock(UserService.class);
        when(userService.getMap()).thenReturn(createMapPinList());
        return userService;
    }

    private static List<MapPin> createMapPinList() {
        ArrayList<MapPin> result = new ArrayList<>();
        MapPin withImage = new MapPin();
        withImage.setImage(IMAGE);
        result.add(withImage);

        result.add(new MapPin());
        return result;
    }

    @Test
    public void effect() {
        UserService userService = mock(UserService.class);
        when(userService.getUserIdByToken(TOKEN)).thenReturn(USER_ID);

        EffectResponse expectedResponse = new EffectResponse();
        when(userService.getEffect(USER_ID)).thenReturn(expectedResponse);

        EffectResponse actualResponse = new UserController(userService, null).effect(TOKEN);
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    public void didItWithCoordinates() {
        DidItRequest didItRequest = new DidItRequest();
        didItRequest.setLongitude(1);
        didItRequest.setLatitude(2);

        UserService userService = mock(UserService.class);
        when(userService.getUserIdByToken(TOKEN)).thenReturn(USER_ID);

        MessageResponse response = new UserController(userService, null).didIt(TOKEN, didItRequest, null);
        assertTrue(response.isSuccess());

        verify(userService).userDidIt(USER_ID, null, didItRequest);
    }
}