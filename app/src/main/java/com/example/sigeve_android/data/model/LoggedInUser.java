package com.example.sigeve_android.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private int rol;

    public LoggedInUser(String userId, String displayName,int rol) {
        this.userId = userId;
        this.displayName = displayName;
        this.rol = rol;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRol() {
        return rol;
    }
}
