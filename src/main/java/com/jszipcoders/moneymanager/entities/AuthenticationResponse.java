package com.jszipcoders.moneymanager.entities;

import java.util.List;

public class AuthenticationResponse {

    private final String jwt;
    private final Long userId;

    public AuthenticationResponse(String jwt, Long userId) {
        this.jwt = jwt;
        this.userId = userId;
    }

    public String getJwt() {
        return jwt;
    }

    public Long getUserId() {
        return userId;
    }

}
