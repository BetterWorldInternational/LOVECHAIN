package org.betterworldinternational.hugapi.route.response;

public class TokenResponse {
    private String token;
    private String inviteCode;

    public TokenResponse(String token, String inviteCode) {
        this.token = token;
        this.inviteCode = inviteCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}