package com.evconnect.models;

public class LoginResponse {
    private String token; // JWT token returned by the server

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
