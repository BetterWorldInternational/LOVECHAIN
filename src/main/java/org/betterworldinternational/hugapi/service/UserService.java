package org.betterworldinternational.hugapi.service;

import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.repository.UserRepository;
import org.betterworldinternational.hugapi.route.request.DidItRequest;
import org.betterworldinternational.hugapi.route.request.RegisterRequest;
import org.betterworldinternational.hugapi.route.response.EffectResponse;
import org.betterworldinternational.hugapi.route.response.MapPin;
import org.betterworldinternational.hugapi.route.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Component
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TokenResponse registerUser(RegisterRequest registerRequest) {
        Integer challengerId = null;

        if (registerRequest.getInvitationPin() != null && !registerRequest.getInvitationPin().isEmpty()) {
            challengerId = getChallenger(registerRequest.getInvitationPin().toUpperCase());
            if (challengerId == null) {
                throw new HugException("Wrong invitation pin");
            }
        }

        if (isEmailUsed(registerRequest.getEmail())) {
            throw new HugException("Email already used");
        }

        String token = UUID.randomUUID().toString();
        String inviteCode = generateInviteCode();

        userRepository.addUser(registerRequest.getUsername(), registerRequest.getEmail(), token, inviteCode, challengerId);

        return new TokenResponse(token, inviteCode);
    }

    public void userDidIt(int userId, String image, DidItRequest didItRequest) {
        if (image == null) {
            image = "default";
        }

        userRepository.userDidIt(userId, image, didItRequest.getLatitude(), didItRequest.getLongitude());
    }

    public List<MapPin> getMap() {
        return userRepository.getMap();
    }

    public EffectResponse getEffect(int userId) {
        EffectResponse effectResponse = new EffectResponse();
        effectResponse.setTotal(userRepository.getEffectCount(userId));
        effectResponse.setActivates(userRepository.getActivatesCount(userId));

        return effectResponse;
    }

    public int getUserIdByToken(String token) {
        int id = userRepository.getUserIdByToken(token);
        if (id == 0) {
            throw new HugException("Unauthorized");
        }

        return id;
    }

    public int getAllActivates() {
        return userRepository.getAllActivatesCount();
    }

    public int getAllEffect() {
        return userRepository.getAllEffectCount();
    }

    public int getEffectByInviteCode(String inviteCode) {
        Integer userId = getChallenger(inviteCode);
        return userId == null ? 0 : getEffect(userId).getTotal();
    }

    public int getActivatesByInviteCode(String inviteCode) {
        Integer userId = getChallenger(inviteCode);
        return userId == null ? 0 : getEffect(userId).getActivates();
    }

    private Integer getChallenger(String inviteCode) {
        return inviteCode == null ? null : userRepository.getUserIdByInviteCode(inviteCode);
    }

    private String generateInviteCode() {
        String invitePin = random5String();
        while (userRepository.getUserIdByInviteCode(invitePin) != null) {
            invitePin = random5String();
        }
        return invitePin;
    }

    private String random5String() {
        String AB = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    private boolean isEmailUsed(String email) {
        return userRepository.getUserIdByEmail(email) != 0;
    }
}