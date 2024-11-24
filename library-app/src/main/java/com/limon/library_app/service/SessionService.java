package com.limon.library_app.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, String> sessionStore = new ConcurrentHashMap<>();

    public void storeSession(String token, String email) {
        sessionStore.put(token, email);
    }


    public String getEmailBySession(String token) {
        return sessionStore.get(token);
    }


    public boolean isValidSession(String token) {
        return sessionStore.containsKey(token);
    }

    public void invalidateSession(String token) {
        sessionStore.remove(token);
    }
}

