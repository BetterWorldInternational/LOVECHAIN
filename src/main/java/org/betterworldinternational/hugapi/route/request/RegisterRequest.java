package org.betterworldinternational.hugapi.route.request;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank
    @Length(min = 3, max = 255)
    private String username;
    @NotBlank
    @Email
    private String email;
    @Length(max = 10)
    private String invitationPin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInvitationPin() {
        return invitationPin;
    }

    public void setInvitationPin(String invitationPin) {
        this.invitationPin = invitationPin;
    }
}