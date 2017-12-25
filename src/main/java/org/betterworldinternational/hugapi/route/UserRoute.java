package org.betterworldinternational.hugapi.route;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.route.request.DidItRequest;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.EffectResponse;
import org.betterworldinternational.hugapi.route.response.MapPin;
import org.betterworldinternational.hugapi.route.response.MessageResponse;
import org.betterworldinternational.hugapi.route.response.TokenResponse;
import org.betterworldinternational.hugapi.service.UserService;
import org.betterworldinternational.hugapi.validation.UserValidation;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Random;

public class UserRoute {
    private final UserService userService;
    private final UserValidation userValidation;
    private final Gson gson;

    public UserRoute(UserService userService, UserValidation userValidation, Gson gson) {
        this.userService = userService;
        this.userValidation = userValidation;
        this.gson = gson;
    }

    public TokenResponse register(Request request, Response response) {
        if (!isValidJson(request.body())) {
            throw new HugException("not valid json request");
        }

        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
        userValidation.validate(registerRequest);

        return userService.registerUser(registerRequest);
    }

    public MessageResponse didIt(Request request, Response response) {
        int userId = userService.getUserIdByToken(request.headers("token"));

        if (!isValidJson(request.body())) {
            throw new HugException("not valid json request");
        }

        DidItRequest didItRequest = gson.fromJson(request.body(), DidItRequest.class);

        if (didItRequest.getLatitude() == 0 || didItRequest.getLongitude() == 0) {

            float random = new Random().nextFloat() * (0.9f - 0.09f) + 0.09f;

            try {

                HttpResponse<JsonNode> res = Unirest.get("http://freegeoip.net/json/{ip}")
                        .routeParam("ip", "188.247.75.179")
                        .asJson();

                double latitude = res.getBody().getObject().getDouble("latitude") + random;
                double longitude = res.getBody().getObject().getDouble("longitude") + random;

                didItRequest.setLatitude(latitude);
                didItRequest.setLongitude(longitude);

            } catch (Exception e) {
                didItRequest.setLatitude(34.049323 + random);
                didItRequest.setLongitude(-118.252965 + random);
            }
        }

        userService.userDidIt(userId, request.queryParams("image"), didItRequest);

        return new MessageResponse(true, "success");
    }

    public List<MapPin> map(Request request, Response response) {
        List<MapPin> mapPins = userService.getMap();
        for (MapPin pin : mapPins) {
            if (pin.getImage() == null) {
                pin.setImage("https://s3.amazonaws.com/hug-challenge/map_pin.png");
            } else {
                pin.setImage("https://s3.amazonaws.com/hug-challenge/" + pin.getImage());
            }
        }
        return mapPins;
    }

    public EffectResponse effect(Request request, Response response) {
        int userId = userService.getUserIdByToken(request.headers("token"));
        return userService.getEffect(userId);
    }

    private boolean isValidJson(String json) {
        try {
            return gson.fromJson(json, Object.class) != null;
        } catch (Exception ex) {
            return false;
        }
    }
}