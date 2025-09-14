package com.evconnect.models;

public interface ServerLoginCallback {
    void onSuccess(String token);
    void onFailure();
}
