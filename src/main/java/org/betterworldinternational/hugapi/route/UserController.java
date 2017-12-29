package org.betterworldinternational.hugapi.route;

import org.betterworldinternational.hugapi.route.request.DidItRequest;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.EffectResponse;
import org.betterworldinternational.hugapi.route.response.MapPin;
import org.betterworldinternational.hugapi.route.response.MessageResponse;
import org.betterworldinternational.hugapi.route.response.TokenResponse;
import org.betterworldinternational.hugapi.service.UserService;
import org.betterworldinternational.hugapi.validation.UserValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserValidation userValidation;

    @Autowired
    public UserController(UserService userService, UserValidation userValidation) {
        this.userService = userService;
        this.userValidation = userValidation;
    }

    @RequestMapping(value = "register/", method = RequestMethod.POST)
    public TokenResponse register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Register request");
        userValidation.validate(registerRequest);

        return userService.registerUser(registerRequest);
    }

    @RequestMapping(value = "didit/", method = RequestMethod.POST)
    public MessageResponse didIt(
            @RequestHeader("token") String token,
            @RequestBody DidItRequest didItRequest,
            @RequestParam(value = "image", required = false) String image) {
        logger.info("DidIt request");

        if (didItRequest.getLatitude() == 0 || didItRequest.getLongitude() == 0) {
            float random = new Random().nextFloat() * (0.9f - 0.09f) + 0.09f;
            didItRequest.setLatitude(34.049323 + random);
            didItRequest.setLongitude(-118.252965 + random);
        }

        int userId = userService.getUserIdByToken(token);
        userService.userDidIt(userId, image, didItRequest);

        return new MessageResponse(true, "success");
    }

    @RequestMapping(value = "map/", method = RequestMethod.GET)
    public List<MapPin> map() {
        logger.info("Map request");
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

    @RequestMapping(value = "effect/", method = RequestMethod.GET)
    public EffectResponse effect(@RequestHeader("token") String token) {
        logger.info("Effect request");
        int userId = userService.getUserIdByToken(token);
        return userService.getEffect(userId);
    }
}